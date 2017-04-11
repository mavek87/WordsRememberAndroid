package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author Matteo Veroni
 */

public abstract class AsyncQuerySearchCommand extends AsyncCommand {

    protected final Uri commandTargetUri;
    private final String[] projection;
    private final String selection;
    private final String[] selectionArgs;
    private final String orderBy;
    private final Object nextCommand;

    protected Cursor queryCompleteCursor;

    public AsyncQuerySearchCommand(ContentResolver contentResolver, Uri commandTargetUri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        this(contentResolver, commandTargetUri, projection, selection, selectionArgs, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncQuerySearchCommand(
            ContentResolver contentResolver,
            Uri commandTargetUri,
            String projection[],
            String selection,
            String[] selectionArgs,
            String orderBy,
            Object nextCommand) {
        super(contentResolver);
        this.commandTargetUri = commandTargetUri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.orderBy = orderBy;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startQuery(0, nextCommand, commandTargetUri, projection, selection, selectionArgs, orderBy);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        queryCompleteCursor = cursor;
        dispatchCompletionEvent();
        executeCommand((AsyncCommand) nextCommand);
    }

    abstract void dispatchCompletionEvent();

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
