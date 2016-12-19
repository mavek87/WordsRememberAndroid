package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Message;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;

import org.greenrobot.eventbus.EventBus;

public class AsyncSaveVocableHandler extends AsyncQueryHandler {
    private EventAsyncSaveVocable event;

    public AsyncSaveVocableHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        try {
            final String createdRowIdUri = uri.getLastPathSegment();
            if (!createdRowIdUri.isEmpty())
                event = new EventAsyncSaveVocable(Long.valueOf(createdRowIdUri));
        } finally {
            if (event == null) event = new EventAsyncSaveVocable(-1);
            EventBus.getDefault().postSticky(event);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
