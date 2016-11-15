package com.matteoveroni.wordsremember.provider.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.matteoveroni.wordsremember.provider.DatabaseManager;

public abstract class DAO {

    private DatabaseManager dbManager;

    public DAO(Context context) {
        dbManager = DatabaseManager.getInstance(context);
    }

    public SQLiteDatabase openDbConnection() {
        return dbManager.getWritableDatabase();
    }

    public void closeDbConnection() {
        dbManager.close();
    }

    public DatabaseManager getDbManager() {
        return this.dbManager;
    }

}
