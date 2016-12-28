package com.matteoveroni.wordsremember.dictionary.models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract.Schema;

/**
 * Class that allows CRUD operations on dictionary data using a content resolver to communicate with
 * the dictionary content provider.
 *
 * @author Matteo Veroni
 */

public class DictionaryDAO {

    public static final String TAG = "DictionaryDAO";

    private final ContentResolver contentResolver;

    private static final Uri CONTENT_PROVIDER_URI = DictionaryProvider.CONTENT_URI;

    public DictionaryDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    /**********************************************************************************************/

    // Async methods

    /**********************************************************************************************/

    public void asyncSaveVocable(Word vocable) {
        // TODO: search a way to avoid to call isVocablePresent synchronous method
        if (isVocableValid(vocable) && vocable.getId() < 0) {
            new AsyncSaveVocableHandler(contentResolver).startInsert(
                    1,
                    null,
                    CONTENT_PROVIDER_URI,
                    vocableToContentValues(vocable)
            );
        }
    }

    public void asyncGetVocableById(long id) {
        if (id > 0) {
            final String str_idColumn = String.valueOf(id);

            final String[] projection = {Schema.COLUMN_NAME};
            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri uri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

            new AsyncGetVocableByIdHandler(contentResolver).startQuery(
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

            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            final Uri uri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_id).buildUpon().build();

            new AsyncUpdateVocableHandler(contentResolver).startUpdate(
                    1,
                    null,
                    uri,
                    vocableToContentValues(updatedVocable),
                    selection,
                    selectionArgs
            );
        }
    }

    public void asyncRemoveVocable(long id) {
        if (id > 0) {
            final String str_idColumn = String.valueOf(id);

            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri uri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

            new AsyncDeleteVocableHandler(contentResolver).startDelete(
                    1,
                    null,
                    uri,
                    selection,
                    selectionArgs
            );
        }
    }

    public static Word cursorToVocable(Cursor cursor) {
        final Word vocable = new Word(cursor.getString(cursor.getColumnIndex(Schema.COLUMN_NAME)));
        vocable.setId(cursor.getLong(cursor.getColumnIndex(Schema.COLUMN_ID)));
        return vocable;
    }

    /**********************************************************************************************/

    // Synchronous methods

    /**********************************************************************************************/

    public Word getVocableById(long id) {
        final String str_id = String.valueOf(id);

        final String[] projection = {Schema.COLUMN_NAME};
        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_id).buildUpon().build();

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
                    CONTENT_PROVIDER_URI,
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

        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_id).buildUpon().build();

        int updatedRecords = contentResolver.update(uri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    public boolean removeVocable(long vocableID) {
        int recordDeleted = 0;
        if (vocableID > 0) {
            final String str_id = String.valueOf(vocableID);

            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_id).buildUpon().build();

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
            values.put(Schema.COLUMN_NAME, vocable.getName());
        }
        return values;
    }

    private boolean isVocableValid(Word vocable) {
        return vocable != null && vocable.getName() != null;
    }
}
