package com.matteoveroni.wordsremember.dictionary.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncUpdateVocableHandler extends AsyncQueryHandler {
    public AsyncUpdateVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
        EventBus.getDefault().postSticky(new EventAsyncUpdateVocableCompleted(numberOfUpdatedRows));
    }
}
