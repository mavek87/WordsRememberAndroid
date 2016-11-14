package com.matteoveroni.wordsremember.database.dao;

import android.content.ContentProvider;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.matteoveroni.wordsremember.database.Database.DatabaseHelper;

public abstract class Dao extends ContentProvider{

    protected SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public Dao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void setDaoContext(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public final void openDbConnection() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public final void closeDbConnection() {
        dbHelper.close();
        db.close();
    }
}
