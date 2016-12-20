package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
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

    public long saveVocable(Word vocable) {
        long id = -1;
        if (isVocableValid(vocable) && !isVocablePresent(vocable)) {
            final Uri createdRowUri = contentResolver.insert(CONTENT_PROVIDER_URI, vocableToContentValues(vocable));

            if (createdRowUri != null) {
                final String createdRowId = createdRowUri.getLastPathSegment();

                if (!createdRowId.isEmpty()) {
                    id = Long.valueOf(createdRowId);
                }
            }
        }
        return id;
    }

    public Word getVocableById(long id) {
        final String str_idColumn = String.valueOf(id);

        final String[] projection = {Schema.COLUMN_NAME};
        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        Cursor cursor = contentResolver.query(
                vocableUri,
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

    public boolean updateVocable(long vocableID, Word newVocable) {
        final String str_idColumn = String.valueOf(vocableID);

        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        int updatedRecords = contentResolver.update(vocableUri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    public boolean removeVocable(long vocableID) {
        int recordDeleted = 0;
        if (vocableID > 0) {
            final String str_idColumn = String.valueOf(vocableID);

            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

            recordDeleted = contentResolver.delete(
                    vocableUri,
                    selection,
                    selectionArgs
            );
        }
        return recordDeleted > 0;
    }

    public void asyncSaveVocable(Word vocable) {
        // TODO: search a way to avoid to call isVocablePresent synchronous method
        if (isVocableValid(vocable) && !isVocablePresent(vocable)) {
            new AsyncSaveVocableHandler(contentResolver).startInsert(
                    1,
                    null,
                    CONTENT_PROVIDER_URI,
                    vocableToContentValues(vocable)
            );
        }
    }

    public void asyncGetVocableById(long id) {
        final String str_idColumn = String.valueOf(id);

        final String[] projection = {Schema.COLUMN_NAME};
        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        new AsyncGetVocableByIdHandler(contentResolver).startQuery(
                1,
                null,
                vocableUri,
                projection,
                selection,
                selectionArgs,
                null
        );
    }

    public void asyncUpdateVocable(long vocableID, Word newVocable) {
        final String str_idColumn = String.valueOf(vocableID);

        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        new AsyncUpdateVocableHandler(contentResolver).startUpdate(
                1,
                null,
                vocableUri,
                vocableToContentValues(newVocable),
                selection,
                selectionArgs
        );
    }

    public void asyncRemoveVocable(long vocableID) {
        if (vocableID > 0) {
            final String str_idColumn = String.valueOf(vocableID);

            final String selection = Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_idColumn};

            final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

            new AsyncDeleteVocableHandler(contentResolver).startDelete(
                    1,
                    null,
                    vocableUri,
                    selection,
                    selectionArgs
            );
        }
    }

    public static Word cursorToVocable(Cursor cursor) {
        return new Word(cursor.getString(0));
    }

    /**
     * Check whether a certain vocable passed was already inserted into the db system or not
     *
     * @param vocable Vocable for check presence
     * @return true if the vocable passed is present or false if it's not
     */
    private boolean isVocablePresent(Word vocable) throws NullPointerException {
        if (isVocableValid(vocable)) {
            boolean isVocablePresent = false;

            final String[] projection = {Schema.COLUMN_NAME};
            final String selection = Schema.COLUMN_NAME + " = ?";
            final String[] selectionArgs = {vocable.getName()};
            final String limit = "1";

            Cursor cursor = contentResolver.query(
                    CONTENT_PROVIDER_URI.buildUpon().appendQueryParameter(DictionaryProvider.QUERY_PARAMETER_LIMIT, limit).build(),
                    projection,
                    selection,
                    selectionArgs,
                    null
            );
            if (cursor != null) {
                isVocablePresent = (cursor.getCount() > 0);
                cursor.close();
            }
            return isVocablePresent;
        } else {
            throw new NullPointerException("Vocable or Vocable Name are null");
        }
    }

    private ContentValues vocableToContentValues(Word vocable) {
        final ContentValues values = new ContentValues();
        values.put(Schema.COLUMN_NAME, vocable.getName());
        return values;
    }

    private boolean isVocableValid(Word vocable) {
        return vocable != null && vocable.getName() != null;
    }
}
