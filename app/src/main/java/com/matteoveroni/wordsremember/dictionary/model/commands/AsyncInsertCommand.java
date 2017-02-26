package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncInsertCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final ContentValues values;
    private final Object nextCommand;

    public AsyncInsertCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values) {
        this(contentResolver, commandTargetUri, values, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.commandTargetUri = commandTargetUri;
        this.values = values;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startInsert(0, null, commandTargetUri, values);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        dispatchCompletionEvent(uri);
        executeCommand((AsyncCommand) nextCommand);
    }

    private void dispatchCompletionEvent(Uri uri) {
        long id = Long.valueOf(uri.getLastPathSegment());
        EventBus.getDefault().postSticky(new EventAsyncSaveVocableCompleted(id));
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
