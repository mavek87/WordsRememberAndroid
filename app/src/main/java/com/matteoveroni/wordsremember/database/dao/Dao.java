package com.matteoveroni.wordsremember.database.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.matteoveroni.wordsremember.database.Database.DatabaseHelper;

public abstract class Dao {

    protected SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public Dao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void openDbConnection() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDbConnection() {
        dbHelper.close();
    }
}
