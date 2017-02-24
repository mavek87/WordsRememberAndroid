package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncUpdateCommand extends AsyncCommand {

    private final CommandTarget commandTarget;
    private final ContentValues values;
    private final Object nextCommand;
    private final String selection;
    private final String[] selectionArgs;

    public AsyncUpdateCommand(ContentResolver contentResolver, CommandTarget commandTarget, ContentValues values, String selection, String[] selectionArgs) {
        this(contentResolver, commandTarget, values, new AsyncNoOperationCommand(contentResolver), selection, selectionArgs);
    }

    public AsyncUpdateCommand(ContentResolver contentResolver, CommandTarget commandTarget, ContentValues values, Object nextCommand, String selection, String[] selectionArgs) {
        super(contentResolver);
        this.commandTarget = commandTarget;
        this.values = values;
        this.nextCommand = nextCommand;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    @Override
    public void execute() {
        startUpdate(0, nextCommand, commandTarget.getContentUri(), values, selection, selectionArgs);
    }

    @Override
    protected void onInsertComplete(int token, Object nextCommand, Uri uriOfInsertedRow) {
        dispatchCompletionEvent();
        ((AsyncCommand) nextCommand).execute();
    }

    private void dispatchCompletionEvent() {
        EventBus.getDefault().postSticky(new EventAsyncUpdateVocableCompleted(1));
    }
}
