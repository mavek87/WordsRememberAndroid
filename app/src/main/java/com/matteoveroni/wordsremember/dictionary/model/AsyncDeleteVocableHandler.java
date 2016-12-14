package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableFailed;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableSuccessful;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableFailed;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableSuccessful;

import org.greenrobot.eventbus.EventBus;

public class AsyncDeleteVocableHandler extends AsyncQueryHandler {
    public AsyncDeleteVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int numberOfDeletedRows) {
        if (numberOfDeletedRows > 0) {
            EventBus.getDefault().postSticky(new EventAsyncDeleteVocableSuccessful());
        } else {
            EventBus.getDefault().postSticky(new EventAsyncDeleteVocableFailed());
        }
    }
}
