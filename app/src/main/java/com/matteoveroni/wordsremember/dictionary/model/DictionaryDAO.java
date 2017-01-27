package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableByIdCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Class that allows CRUD operations on dictionary data using a content resolver to communicate with
 * the dictionary content provider.
 *
 * @author Matteo Veroni
 */

public class DictionaryDAO {

    private final ContentResolver contentResolver;
    private final AsyncVocableHandler asyncVocableHandler;

    public DictionaryDAO(Context context) {
        this.contentResolver = context.getContentResolver();
        this.asyncVocableHandler = new AsyncVocableHandler(this.contentResolver);
    }

    /**********************************************************************************************/

    // Async methods

    /**********************************************************************************************/

    public void asyncSaveVocable(Word vocable) {
        if (isVocableValid(vocable) && vocable.getId() < 0) {
            new AsyncVocableHandler(contentResolver).startInsert(
                    1,
                    null,
                    VocablesContract.CONTENT_URI,
                    vocableToContentValues(vocable)
            );
        }
    }

    public void asyncGetVocableById(long id) {
        if (id > 0) {
            final String str_idColumn = String.valueOf(id);

            final String[] projection = {VocablesContract.Schema.COLUMN_VOCABLE};
            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_idColumn).buildUpon().build();

            asyncVocableHandler.startQuery(
                    1,
                    null,
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );
        }
    }

    public void asyncUpdateVocable(long id, Word updatedVocable) {
        if (id > 0) {
            final String str_id = String.valueOf(id);

            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

            asyncVocableHandler.startUpdate(
                    1,
                    null,
                    uri,
                    vocableToContentValues(updatedVocable),
                    selection,
                    selectionArgs
            );
        }
    }

    public void asyncDeleteVocable(long id) {
        if (id > 0) {
            final String str_idColumn = String.valueOf(id);

            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_idColumn).buildUpon().build();

            asyncVocableHandler.startDelete(
                    1,
                    null,
                    uri,
                    selection,
                    selectionArgs
            );
        }
    }

    public static Word cursorToVocable(Cursor cursor) {
        final Word vocable = new Word(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE)));
        vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID)));
        return vocable;
    }

    /**********************************************************************************************/

    // Synchronous methods

    /**********************************************************************************************/

    public Word getVocableById(long id) {
        final String str_id = String.valueOf(id);

        final String[] projection = {VocablesContract.Schema.COLUMN_VOCABLE};
        final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

        Cursor cursor = contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            return cursorToVocable(cursor);
        } else {
            throw new RuntimeException("duplicated ids for different vocables");
        }
    }

    public long saveVocable(Word vocable) {
        long id = -1;
        if (isVocableValid(vocable) && vocable.getId() < 0) {
            final Uri uri = contentResolver.insert(
                    VocablesContract.CONTENT_URI,
                    vocableToContentValues(vocable)
            );

            if (uri != null) {
                final String createdRowId = uri.getLastPathSegment();

                if (!createdRowId.isEmpty()) {
                    id = Long.valueOf(createdRowId);
                }
            }
        }
        return id;
    }

    public boolean updateVocable(long vocableID, Word newVocable) {
        final String str_id = String.valueOf(vocableID);

        final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

        int updatedRecords = contentResolver.update(uri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    public boolean removeVocable(long vocableID) {
        int recordDeleted = 0;
        if (vocableID > 0) {
            final String str_id = String.valueOf(vocableID);

            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            final Uri vocableUri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

            recordDeleted = contentResolver.delete(
                    vocableUri,
                    selection,
                    selectionArgs
            );
        }
        return recordDeleted > 0;
    }

    /**********************************************************************************************/

    // Helper methods

    /**********************************************************************************************/

    private ContentValues vocableToContentValues(Word vocable) {
        final ContentValues values = new ContentValues();
        if (isVocableValid(vocable)) {
            values.put(VocablesContract.Schema.COLUMN_VOCABLE, vocable.getName());
        }
        return values;
    }

    private boolean isVocableValid(Word vocable) {
        return vocable != null && vocable.getName() != null;
    }

    /**
     * AsyncVocableHandler private inner class
     */
    private class AsyncVocableHandler extends AsyncQueryHandler {

        private final EventBus eventBus = EventBus.getDefault();

        AsyncVocableHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            EventAsyncGetVocableByIdCompleted event;
            if (cursor == null) {
                event = new EventAsyncGetVocableByIdCompleted(null);
            } else {
                cursor.moveToFirst();
                Word vocable = DictionaryDAO.cursorToVocable(cursor);
                event = new EventAsyncGetVocableByIdCompleted(vocable);
            }
            eventBus.postSticky(event);
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            EventAsyncSaveVocableCompleted event = null;
            try {
                final String createdRowIdUri = uri.getLastPathSegment();
                if (!createdRowIdUri.isEmpty())
                    event = new EventAsyncSaveVocableCompleted(Long.valueOf(createdRowIdUri));
            } finally {
                if (event == null)
                    event = new EventAsyncSaveVocableCompleted(-1);
                eventBus.postSticky(event);
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int numberOfUpdatedRows) {
            eventBus.postSticky(new EventAsyncUpdateVocableCompleted(numberOfUpdatedRows));
        }


        @Override
        protected void onDeleteComplete(int token, Object cookie, int numberOfDeletedRows) {
            eventBus.postSticky(new EventAsyncDeleteVocableCompleted(numberOfDeletedRows));
        }
    }
}


