package com.matteoveroni.wordsremember.dictionary.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */
public class AsyncInsertCommand extends CompletionHandler {

    private final EventBus eventBus = EventBus.getDefault();
    private Uri content_uri;
    private final Type type;
    private final ContentValues values;
    private final Object nextCommand;

    public AsyncInsertCommand(ContentResolver contentResolver, Type type, ContentValues values) {
        this(contentResolver, type, values, new NoOperationCommand(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, Type type, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.type = type;
        switch (type) {
            case vocable:
                content_uri = VocablesContract.CONTENT_URI;
                break;
            case translation:
                content_uri = TranslationsContract.CONTENT_URI;
                break;
            case vocableTranslation:
                content_uri = VocablesTranslationsContract.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown type for async insertion command");
        }
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
        switch (type) {
            case vocable:
                dispatchCompletionEventForVocable(uriOfInsertedRow);
                break;
            case translation:
                dispatchCompletionEventForTranslation(uriOfInsertedRow);
                break;
            case vocableTranslation:
                dispatchCompletionEventForVocableTranslation(uriOfInsertedRow);
                break;
        }
    }

    private void dispatchCompletionEventForVocable(Uri uriOfInsertedRow) {
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

    private void dispatchCompletionEventForTranslation(Uri uriOfInsertedRow) {
    }

    private void dispatchCompletionEventForVocableTranslation(Uri uriOfInsertedRow) {
    }
}
