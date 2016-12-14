package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.matteoveroni.wordsremember.events.EventAsyncGetVocableByIdFailed;
import com.matteoveroni.wordsremember.events.EventAsyncGetVocableByIdSuccessful;
import com.matteoveroni.wordsremember.models.Word;

import org.greenrobot.eventbus.EventBus;

public class AsyncGetVocableByIdQueryHandler extends AsyncQueryHandler {

    public AsyncGetVocableByIdQueryHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cursor == null || cursor.getCount() != 1) {
            EventBus.getDefault().postSticky(new EventAsyncGetVocableByIdFailed());
        } else {
            cursor.moveToFirst();
            Word vocable = DictionaryDAO.cursorToVocable(cursor);
            EventBus.getDefault().postSticky(new EventAsyncGetVocableByIdSuccessful(vocable));
        }
    }
}
