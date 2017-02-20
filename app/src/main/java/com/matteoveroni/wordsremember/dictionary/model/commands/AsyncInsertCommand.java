package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

/**
 * @author Matteo Veroni
 */

public class AsyncInsertCommand extends AsyncCommand {

    private final CommandTarget commandTarget;
    private final ContentValues values;
    private final Object nextCommand;

    public AsyncInsertCommand(ContentResolver contentResolver, CommandTarget commandTarget, ContentValues values) {
        this(contentResolver, commandTarget, values, new AsynCommandNoOperation(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, CommandTarget commandTarget, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.commandTarget = commandTarget;
        this.values = values;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startInsert(0, nextCommand, commandTarget.getContentUri(), values);
    }

    @Override
    protected void onInsertComplete(int token, Object nextCommand, Uri uriOfInsertedRow) {
        dispatchCompletionEvent(uriOfInsertedRow);
        ((AsyncCommand) nextCommand).execute();
    }

    private void dispatchCompletionEvent(Uri uriOfInsertedRow) {
        commandTarget.dispatchCompletionEvent(uriOfInsertedRow);
    }
}
