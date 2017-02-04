package com.matteoveroni.wordsremember.dictionary.model.async_commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */
public class AsyncInsertCommand extends CompletionHandler {

    private final EventBus eventBus = EventBus.getDefault();

    private final Uri content_uri;
    private final ContentValues values;
    private final Object nextCommand;

    public AsyncInsertCommand(ContentResolver contentResolver, Uri content_uri, ContentValues values) {
        this(contentResolver, content_uri, values, new NoOperationCommand(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, Uri content_uri, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.content_uri = content_uri;
        this.values = values;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startInsert(0, nextCommand, content_uri, values);
    }

    @Override
    protected void onInsertComplete(int token, Object nextCommand, Uri uriOfInsertedRow) {
        dispatchCompletionEvent(uriOfInsertedRow);
        ((CompletionHandler) nextCommand).execute();
    }

    private void dispatchCompletionEvent(Uri uriOfInsertedRow) {
        // Todo: handle translations insertions too
        EventAsyncSaveVocableCompleted event = null;
        String errorMessage = "Insertion failed";
        try {
            String idOfInsertedRow = uriOfInsertedRow.getLastPathSegment();
            if (!idOfInsertedRow.isEmpty())
                event = new EventAsyncSaveVocableCompleted(Long.valueOf(idOfInsertedRow));

        } catch (Exception ex) {
            errorMessage += "\n" + ex.getMessage();
        } finally {
            if (event == null) {
                event = new EventAsyncSaveVocableCompleted(-1);
                event.setErrorMessage(errorMessage);
            }
        }
        eventBus.postSticky(event);
    }
}
