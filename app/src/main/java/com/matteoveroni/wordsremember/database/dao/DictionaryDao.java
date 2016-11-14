package com.matteoveroni.wordsremember.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.matteoveroni.wordsremember.database.tables.TableDictionary;
import com.matteoveroni.wordsremember.model.Dictionary;
import com.matteoveroni.wordsremember.model.Word;

import java.util.ArrayList;
import java.util.List;

public class DictionaryDao extends Dao {

    public DictionaryDao(Context context) {
        super(context);
    }

    private static final String NULL_VOCABLE_POINTER_EXCEPTION = "Vocable or Vocable Name are null";

    /**
     * Check whether a certain vocable passed was already inserted into the db system or not
     *
     * @param vocable
     * @return true if the vocable passed is present or false if the case is the opposite
     */
    public boolean isVocablePresent(Word vocable) throws NullPointerException {
        if (!isVocableValid(vocable)) {
            throw new NullPointerException(NULL_VOCABLE_POINTER_EXCEPTION);
        } else {
            boolean isVocablePresent;
            final String[] usedColumns = {TableDictionary.COLUMN_WORD_NAME};
            final String selection = TableDictionary.COLUMN_WORD_NAME + " =?";
            final String[] selectionArgs = {vocable.getName()};
            final String limit = "1";

            openDbConnection();
            Cursor cursor = db.query(TableDictionary.NAME, usedColumns, selection, selectionArgs, null, null, null, limit);
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
            values.put(TableDictionary.COLUMN_WORD_NAME, vocable.getName());

            openDbConnection();
            vocableId = db.insert(TableDictionary.NAME, null, values);
            closeDbConnection();
        }
        return vocableId;
    }

    public Cursor getAllVocables() {
        return db.query(TableDictionary.NAME, new String[]{TableDictionary.COLUMN_WORD_NAME}, null, null, null, null, null);
    }

    public List<Word> getAllVocablesList() {
        List<Word> vocables = new ArrayList<>();

        openDbConnection();
        Cursor cursor = db.query(TableDictionary.NAME, TableDictionary.ALL_COLUMNS, null, null, null, null, null);
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
