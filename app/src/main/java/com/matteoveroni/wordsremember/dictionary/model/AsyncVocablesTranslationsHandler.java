package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */
class AsyncVocablesTranslationsHandler extends AsyncQueryHandler {

    private final EventBus eventBus = EventBus.getDefault();

    AsyncVocablesTranslationsHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int numberOfDeletedRows) {
    }

}
