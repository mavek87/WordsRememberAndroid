package com.matteoveroni.wordsremember.activities.dictionary_management.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.activities.dictionary_management.DictionaryModel;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract.Schema;

/**
 * Class that offers CRUD operations on dictionary data using a content resolver to communicate with
 * the dictionary content provider.
 *
 * @author Matteo Veroni
 */

public class DictionaryDAO implements DictionaryModel {

    private final ContentResolver contentResolver;

    private static final Uri CONTENT_PROVIDER_URI = DictionaryProvider.CONTENT_URI;

    public DictionaryDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    private static final String VOCABLE_NULL_POINTER_EXCEPTION = "Vocable or Vocable Name are null";

    /**
     * Insert a vocable into the dictionary if it's not present
     *
     * @param vocable The vocable to insert into the dictionary
     * @return A long number that is negative if an error occurred during the insertion operation or
     * is positive and corresponds to the inserted vocable id if the operation was successful.
     * @throws NullPointerException
     */
    @Override
    public long saveVocable(Word vocable) throws NullPointerException {
        long id = -1;
        if (!isVocablePresent(vocable)) {
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

    @Override
    public boolean updateVocable(long vocableID, Word newVocable) {
        final String str_idColumn = String.valueOf(vocableID);

        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        int updatedRecords = contentResolver.update(vocableUri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    /**
     * Retrieve and get a vocable from the db using his ID
     *
     * @param id The vocable ID to search into the database
     * @return The vocable corresponding to the searched ID or null if the database doesn't contain any
     * vocable with that ID.
     */
    @Override
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

        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            return cursorToVocable(cursor);
        } else {
            throw new RuntimeException("duplicated ids for different vocables");
        }
    }

    /**
     * Remove a vocable from the dictionary using the vocable ID
     *
     * @param vocableID The vocable ID
     * @return True if the vocable has been removed and false in the other case
     */
    @Override
    public boolean removeVocable(long vocableID) {
        final String str_idColumn = String.valueOf(vocableID);

        final String selection = Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_idColumn};

        final Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        final int recordDeleted = contentResolver.delete(
                vocableUri,
                selection,
                selectionArgs
        );

        return recordDeleted > 0;
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
            throw new NullPointerException(VOCABLE_NULL_POINTER_EXCEPTION);
        }
    }

    private Word cursorToVocable(Cursor cursor) {
        return new Word(cursor.getString(0));
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
