package com.matteoveroni.wordsremember.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DictionaryDao extends Dao {

    public DictionaryDao(Context context) {
        super(context);
    }

//    public long saveNote(Note note) {
//        ContentValues values = new ContentValues();
//        values.put(TableNote.COLUMN_TITLE, note.getTitle());
//        values.put(TableNote.COLUMN_MESSAGE, note.getMessage());
//        values.put(TableNote.COLUMN_CATEGORY, note.getCategory().toString());
//        values.put(TableNote.COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");
//
//        openDbConnection();
//        long noteSavedId = db.insert(TableNote.NAME, null, values);
//        closeDbConnection();
//
//        return noteSavedId;
//    }
//
//    public List<Note> getAllNotes() {
//        List<Note> notes = new ArrayList<>();
//
//        openDbConnection();
//        Cursor cursor = db.query(TableNote.NAME, TableNote.COLUMNS, null, null, null, null, null);
//
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            notes.add(cursorToNote(cursor));
//        }
//        cursor.close();
//        closeDbConnection();
//        return notes;
//    }
//
//    private Note cursorToNote(Cursor cursor) {
//        return new Note(
//                cursor.getString(1),                            // Titolo
//                cursor.getString(2),                            // Messaggio
//                Note.Category.valueOf(cursor.getString(3)),     // Categoria
//                cursor.getLong(0),                              // Id
//                cursor.getLong(4)                               // Data
//        );
//    }

}
