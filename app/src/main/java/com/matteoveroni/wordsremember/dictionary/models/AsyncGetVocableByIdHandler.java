package com.matteoveroni.wordsremember.dictionary.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableById;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;

public class AsyncGetVocableByIdHandler extends AsyncQueryHandler {

    public AsyncGetVocableByIdHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final EventAsyncGetVocableById event;
        if (cursor == null) {
            event = new EventAsyncGetVocableById(null);
        } else {
            cursor.moveToFirst();
            Word vocable = DictionaryDAO.cursorToVocable(cursor);
            event = new EventAsyncGetVocableById(vocable);
        }
        EventBus.getDefault().postSticky(event);
    }
}
