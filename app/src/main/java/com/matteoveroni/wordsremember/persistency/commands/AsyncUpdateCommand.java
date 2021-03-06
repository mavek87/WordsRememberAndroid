package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncUpdateVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncUpdateCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final ContentValues values;
    private final String selection;
    private final String[] selectionArgs;
    private final Object nextCommand;

    private int updateResult;

    public AsyncUpdateCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values, String selection, String[] selectionArgs) {
        this(contentResolver, commandTargetUri, values, selection, selectionArgs, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncUpdateCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values, String selection, String[] selectionArgs, Object nextCommand) {
        super(contentResolver);
        this.commandTargetUri = commandTargetUri;
        this.values = values;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startUpdate(0, nextCommand, commandTargetUri, values, selection, selectionArgs);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        updateResult = result;
        dispatchCompletionEvent();
        executeAsyncCommand((AsyncCommand) nextCommand);
    }

    @Override
    public void dispatchCompletionEvent() {
        if (commandTargetUri.equals(VocablesContract.CONTENT_URI)) {
            EventBus.getDefault().postSticky(new EventAsyncUpdateVocableCompleted(updateResult));
        }
    }

    private void executeAsyncCommand(AsyncCommand asyncCommand) {
        asyncCommand.execute();
    }
}
