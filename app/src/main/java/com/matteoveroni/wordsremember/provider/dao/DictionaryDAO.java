package com.matteoveroni.wordsremember.provider.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract.Schema;

/**
 * @author Matteo Veroni
 */
public class DictionaryDAO {

    private final Context context;
    private final ContentResolver contentResolver;

    private static final Uri CONTENT_PROVIDER_URI = DictionaryProvider.CONTENT_URI;

    public DictionaryDAO(Context context) {
        this.context = context;
        this.contentResolver = this.context.getContentResolver();
    }

    private static final String VOCABLE_NULL_POINTER_EXCEPTION = "Vocable or Vocable Name are null";

    /**
     * Check whether a certain vocable passed was already inserted into the db system or not
     *
     * @param vocable Vocable for presence check
     * @return true if the vocable passed is present or false if the case is the opposite
     */
    public boolean isVocablePresent(Word vocable) throws NullPointerException {
        if (!isVocableValid(vocable)) {
            throw new NullPointerException(VOCABLE_NULL_POINTER_EXCEPTION);
        } else {
            boolean isVocablePresent;
            final String[] projection = {Schema.COLUMN_NAME};
            final String selection = Schema.COLUMN_NAME + " =?";
            final String[] selectionArgs = {vocable.getName()};
            final String limit = "1";

            Cursor cursor = contentResolver.query(
                    CONTENT_PROVIDER_URI.buildUpon().appendQueryParameter(DictionaryProvider.QUERY_PARAMETER_LIMIT, limit).build(),
                    projection,
                    selection,
                    selectionArgs,
                    null
            );
            isVocablePresent = (cursor.getCount() > 0);
            cursor.close();
            return isVocablePresent;
        }
    }

    public long saveVocable(Word vocable) throws NullPointerException {
        long id = -1;
        if (!isVocablePresent(vocable)) {
            ContentValues values = new ContentValues();
            values.put(Schema.COLUMN_NAME, vocable.getName());

            Uri createdRowUri = contentResolver.insert(CONTENT_PROVIDER_URI, values);
            String createdRowId = createdRowUri.getLastPathSegment();

            if (!createdRowId.isEmpty()) {
                id = Long.valueOf(createdRowId);
            }
        }
        return id;
    }

    public Word getVocableById(long id) {
        String str_idColumn = String.valueOf(id);

        final String[] projection = {Schema.COLUMN_NAME};
        final String selection = Schema.COLUMN_ID + " =?";
        final String[] selectionArgs = {str_idColumn};

        Uri vocableUri = Uri.withAppendedPath(CONTENT_PROVIDER_URI, str_idColumn).buildUpon().build();

        Cursor cursor = contentResolver.query(
                vocableUri,
                projection,
                selection,
                selectionArgs,
                null
        );

        final Word vocable;
        if (cursor.getCount() == 1) {
            vocable = cursorToVocable(cursor);
        } else {
            throw new RuntimeException("duplicated ids for different vocables");
        }
        return vocable;
    }

//    public boolean removeVocable(Word vocable) throws NullPointerException {
//        final SQLiteDatabase db = openDbConnection();
//        boolean recordDeleted = db.delete(Schema.TABLE_NAME, Schema.COLUMN_NAME + " = ?", new String[]{vocable.getName()}) > 0;
//        closeDbConnection();
//        return recordDeleted;
//    }
//
//    /**
//     * TODO: think how to close the db (if I close db here the cursor will not work outside)
//     * used for Cursor Loader Manager
//     *
//     * @return Cursor Cursor containing all the vocables found
//     */
//    public Cursor getAllVocables() {
//        return openDbConnection().query(Schema.TABLE_NAME, new String[]{Schema.COLUMN_NAME}, null, null, null, null, null);
//    }
//
//    public List<Word> getAllVocablesList() {
//        List<Word> vocables = new ArrayList<>();
//
//        Cursor cursor = openDbConnection().query(Schema.TABLE_NAME, Schema.ALL_COLUMNS, null, null, null, null, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            vocables.add(cursorToVocable(cursor));
//        }
//        cursor.close();
//        closeDbConnection();
//
//        return vocables;
//    }

    private Word cursorToVocable(Cursor cursor) {
        return new Word(cursor.getString(1));
    }

    private boolean isVocableValid(Word vocable) {
        return vocable != null && vocable.getName() != null;
    }
}
