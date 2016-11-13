package com.matteoveroni.wordsremember.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.wordsremember.database.tables.TableDictionary;
import com.matteoveroni.wordsremember.database.tables.TableDictionaryTranslations;
import com.matteoveroni.wordsremember.database.tables.TableTranslations;

public class Database {
    public static final String NAME = "notebook.db";
    public static final int VERSION = 1;

    public static final class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, Database.NAME, null, Database.VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createAllTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(this.getClass().toString(), "Updating db from old version " + oldVersion + " to new version " + newVersion);
            dropAllTables(db);
            createAllTables(db);
        }

        private void createAllTables(SQLiteDatabase db) {
            TableDictionary.createTable(db);
            TableTranslations.createTable(db);
            TableDictionaryTranslations.createTable(db);
        }

        private void dropAllTables(SQLiteDatabase db) {
            TableDictionary.dropTable(db);
            TableTranslations.dropTable(db);
            TableDictionaryTranslations.dropTable(db);
        }
    }
}
