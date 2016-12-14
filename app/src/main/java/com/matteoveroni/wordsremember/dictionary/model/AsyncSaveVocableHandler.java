package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableFailed;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableSuccessful;

import org.greenrobot.eventbus.EventBus;

public class AsyncSaveVocableHandler extends AsyncQueryHandler {
    public AsyncSaveVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri createdRowUri) {
        long id;
        if (createdRowUri != null) {
            final String createdRowId = createdRowUri.getLastPathSegment();
            if (!createdRowId.isEmpty()) {
                id = Long.valueOf(createdRowId);
                EventBus.getDefault().postSticky(new EventAsyncSaveVocableSuccessful(id));
                return;
            }
            EventBus.getDefault().postSticky(new EventAsyncSaveVocableFailed());
        }
    }
}
