package com.matteoveroni.wordsremember.dictionary.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Message;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncSaveVocableHandler extends AsyncQueryHandler {
    private EventAsyncSaveVocableCompleted event;

    public AsyncSaveVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        try {
            final String createdRowIdUri = uri.getLastPathSegment();
            if (!createdRowIdUri.isEmpty())
                event = new EventAsyncSaveVocableCompleted(Long.valueOf(createdRowIdUri));
        } finally {
            if (event == null) event = new EventAsyncSaveVocableCompleted(-1);
            EventBus.getDefault().postSticky(event);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
