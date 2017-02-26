package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncGetVocableByIdCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

// TODO: fix this class using new AsyncCommand and CommandTarget api\'s
public class AsyncQueryCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final String[] projection;
    private final String selection;
    private final String[] selectionArgs;
    private final String orderBy;
    private final Object nextCommand;

    public AsyncQueryCommand(ContentResolver contentResolver, Uri commandTargetUri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        this(contentResolver, commandTargetUri, projection, selection, selectionArgs, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncQueryCommand(
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
        dispatchCompletionEvent(cursor);
        executeCommand((AsyncCommand) nextCommand);
    }

    private void dispatchCompletionEvent(Cursor cursor) {
        EventAsyncGetVocableByIdCompleted event;
        if (cursor == null) {
            event = new EventAsyncGetVocableByIdCompleted(null);
        } else {
            cursor.moveToFirst();
            Word vocable = DictionaryDAO.cursorToVocable(cursor);
            event = new EventAsyncGetVocableByIdCompleted(vocable);
        }
        EventBus.getDefault().postSticky(event);
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
