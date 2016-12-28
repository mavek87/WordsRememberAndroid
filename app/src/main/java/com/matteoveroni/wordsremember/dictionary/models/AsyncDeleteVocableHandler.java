package com.matteoveroni.wordsremember.dictionary.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;

import org.greenrobot.eventbus.EventBus;

public class AsyncDeleteVocableHandler extends AsyncQueryHandler {
    public AsyncDeleteVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int numberOfDeletedRows) {
        EventBus.getDefault().postSticky(new EventAsyncDeleteVocableCompleted(numberOfDeletedRows));
    }
}
