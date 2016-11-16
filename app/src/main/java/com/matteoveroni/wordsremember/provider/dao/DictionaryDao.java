package com.matteoveroni.wordsremember.provider.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract.Schema;

import java.util.ArrayList;
import java.util.List;

public class DictionaryDAO extends DAO {

    public DictionaryDAO(Context context) {
        super(context);
    }

    private static final String NULL_VOCABLE_POINTER_EXCEPTION = "Vocable or Vocable Name are null";

    /**
     * Check whether a certain vocable passed was already inserted into the db system or not
     *
     * @param vocable Vocable for presence check
     * @return true if the vocable passed is present or false if the case is the opposite
     */
    public boolean isVocablePresent(Word vocable) throws NullPointerException {
        if (!isVocableValid(vocable)) {
            throw new NullPointerException(NULL_VOCABLE_POINTER_EXCEPTION);
        } else {
            boolean isVocablePresent;
            final String[] usedColumns = {Schema.COLUMN_NAME};
            final String selection = Schema.COLUMN_NAME + " =?";
            final String[] selectionArgs = {vocable.getName()};
            final String limit = "1";

            Cursor cursor = openDbConnection().query(Schema.TABLE_NAME, usedColumns, selection, selectionArgs, null, null, null, limit);
            isVocablePresent = (cursor.getCount() > 0);
            cursor.close();
            closeDbConnection();

            return isVocablePresent;
        }
    }

    public long saveVocable(Word vocable) throws NullPointerException {
        long vocableId = -1;
        if (!isVocablePresent(vocable)) {
            ContentValues values = new ContentValues();
            values.put(Schema.COLUMN_NAME, vocable.getName());

            vocableId = openDbConnection().insert(Schema.TABLE_NAME, null, values);
            closeDbConnection();
        }
        return vocableId;
    }

    public boolean removeVocable(Word vocable) throws NullPointerException {
        final SQLiteDatabase db = openDbConnection();
        boolean recordDeleted = db.delete(Schema.TABLE_NAME, Schema.COLUMN_NAME + " = ?", new String[]{vocable.getName()}) > 0;
        closeDbConnection();
        return recordDeleted;
    }

    /**
     * TODO: think how to close the db (if I close db here the cursor will not work outside)
     * used for Cursor Loader Manager
     *
     * @return Cursor Cursor containing all the vocables found
     */
    public Cursor getAllVocables() {
        return openDbConnection().query(Schema.TABLE_NAME, new String[]{Schema.COLUMN_NAME}, null, null, null, null, null);
    }

    public List<Word> getAllVocablesList() {
        List<Word> vocables = new ArrayList<>();

        Cursor cursor = openDbConnection().query(Schema.TABLE_NAME, Schema.ALL_COLUMNS, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            vocables.add(cursorToVocable(cursor));
        }
        cursor.close();
        closeDbConnection();

        return vocables;
    }

    private Word cursorToVocable(Cursor cursor) {
        return new Word(
                cursor.getString(1)
        );
    }

    private boolean isVocableValid(Word vocable) {
        return vocable != null && vocable.getName() != null;
    }

}
