package com.matteoveroni.wordsremember.dictionary.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocable;

import org.greenrobot.eventbus.EventBus;

public class AsyncUpdateVocableHandler extends AsyncQueryHandler {
    public AsyncUpdateVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
        EventBus.getDefault().postSticky(new EventAsyncUpdateVocable(numberOfUpdatedRows));
    }
}
