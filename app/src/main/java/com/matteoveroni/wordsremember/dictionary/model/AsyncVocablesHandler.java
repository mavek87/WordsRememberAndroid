package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncVocableDeletionComplete;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableByIdCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

//TODO: remove this class
class AsyncVocablesHandler extends AsyncQueryHandler {

    private final EventBus eventBus = EventBus.getDefault();

    AsyncVocablesHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
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

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        EventAsyncSaveVocableCompleted event = null;
        try {
            final String createdRowIdUri = uri.getLastPathSegment();
            if (!createdRowIdUri.isEmpty())
                event = new EventAsyncSaveVocableCompleted(Long.valueOf(createdRowIdUri));
        } finally {
            if (event == null)
                event = new EventAsyncSaveVocableCompleted(-1);
            eventBus.postSticky(event);
        }
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
        eventBus.postSticky(new EventAsyncUpdateVocableCompleted(numberOfUpdatedRows));
    }


    @Override
    protected void onDeleteComplete(int token, Object cookie, int numberOfDeletedRows) {
        eventBus.postSticky(new EventAsyncVocableDeletionComplete(numberOfDeletedRows));
    }
}
