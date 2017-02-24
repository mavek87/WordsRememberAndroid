package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncInsertCommand extends AsyncCommand {

    private final CommandTarget target;
    private final ContentValues values;
    private final Object nextCommand;

    public AsyncInsertCommand(ContentResolver contentResolver, CommandTarget target, ContentValues values) {
        this(contentResolver, target, values, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, CommandTarget target, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.target = target;
        this.values = values;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startInsert(0, nextCommand, target.getContentUri(), values);
    }

    @Override
    protected void onInsertComplete(int token, Object nextCommand, Uri uriOfInsertedRow) {
        dispatchCompletionEvent(uriOfInsertedRow);
        executeNextCommand();
    }

    private void executeNextCommand() {
        ((AsyncCommand) nextCommand).execute();
    }

    private void dispatchCompletionEvent(Uri uriOfInsertedRow) {
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
        EventBus.getDefault().postSticky(event);
    }
}
