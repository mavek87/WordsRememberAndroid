package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableFailed;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableSuccessful;

import org.greenrobot.eventbus.EventBus;

public class AsyncUpdateVocableHandler extends AsyncQueryHandler {
    public AsyncUpdateVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
        if (numberOfUpdatedRows > 0) {
            EventBus.getDefault().postSticky(new EventAsyncUpdateVocableSuccessful());
        } else {
            EventBus.getDefault().postSticky(new EventAsyncUpdateVocableFailed());
        }
    }
}
