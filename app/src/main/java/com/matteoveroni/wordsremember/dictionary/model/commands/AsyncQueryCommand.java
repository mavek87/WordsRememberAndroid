package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableByIdCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

// TODO: fix this class using new AsyncCommand and CommandTarget api\'s
public class AsyncQueryCommand extends AsyncCommand {

    private final EventBus eventBus = EventBus.getDefault();

    private final Uri content_uri;
    private final String[] projection;
    private final String selection;
    private final String[] selectionArgs;
    private final String orderBy;
    private final Object nextCommand;

    public AsyncQueryCommand(ContentResolver contentResolver, Uri content_uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        this(contentResolver, content_uri, projection, selection, selectionArgs, orderBy, new AsynCommandNoOperation(contentResolver));
    }

    public AsyncQueryCommand(
            ContentResolver contentResolver,
            Uri content_uri, String projection[],
            String selection,
            String[] selectionArgs,
            String orderBy,
            Object nextCommand) {
        super(contentResolver);
        this.content_uri = content_uri;
        this.projection = projection;
        this.nextCommand = nextCommand;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.orderBy = orderBy;
    }

    @Override
    public void execute() {
        startQuery(0, nextCommand, content_uri, projection, selection, selectionArgs, orderBy);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        dispatchCompletionEvent(cursor);
        ((AsyncCommand) nextCommand).execute();
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
        eventBus.postSticky(event);
    }
}
