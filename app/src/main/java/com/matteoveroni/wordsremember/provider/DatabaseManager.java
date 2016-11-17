package com.matteoveroni.wordsremember.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseManager";
    public static final String NAME = "wordsremember.db";
    public static final int VERSION = 1;

    private static DictionaryContract.Schema DICTIONARY_SCHEMA;
    private static TranslationsContract.Schema TRANSLATIONS_SCHEMA;

    private static final class SQL_QUERIES {
        private static final String CREATE_DICTIONARY_TABLE = "create table "
                + DICTIONARY_SCHEMA.TABLE_NAME + " ( "
                + DICTIONARY_SCHEMA.COLUMN_ID + " integer primary key autoincrement, "
                + DICTIONARY_SCHEMA.COLUMN_NAME + " text not null"
                + " );";

        private static final String DROP_DICTIONARY_TABLE = "drop table if exists " + DICTIONARY_SCHEMA.TABLE_NAME;

        private static final String CREATE_TRANSLATIONS_TABLE = "create table "
                + TRANSLATIONS_SCHEMA.TABLE_NAME + " ( "
                + TRANSLATIONS_SCHEMA.COLUMN_ID + " integer primary key autoincrement, "
                + TRANSLATIONS_SCHEMA.COLUMN_NAME + " text not null"
                + " );";

        private static final String DROP_TRANSLATIONS_TABLE = "drop table if exists " + TRANSLATIONS_SCHEMA.TABLE_NAME;
    }

    /**
     * Unique DatabaseManager instance
     */
    private volatile static DatabaseManager DB_MANAGER;

    /**
     * DatabaseMaanager private constructor
     *
     * @param context
     */
    private DatabaseManager(Context context) {
        super(context, DatabaseManager.NAME, null, DatabaseManager.VERSION);
    }

    /**
     * Get or create a unique DatabaseManager instance using the singleton pattern
     *
     * @param context
     * @return Unique DatabaseManager instance
     */
    public static final DatabaseManager getInstance(Context context) {
        if (DB_MANAGER == null) {
            synchronized (DatabaseManager.class) {
                if (DB_MANAGER == null) {
                    DB_MANAGER = new DatabaseManager(context);
                }
            }
        }
        return DB_MANAGER;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Updating db from old version " + oldVersion + " to new version " + newVersion);
        dropAllTables(db);
        createAllTables(db);
    }

    /**
     * drop all the tables and their content and recreate them
     */
    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
    }

    /**
     * TODO: test this method
     */
    public void exportDBOnSD() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.matteoveroni.wordsremember" + "/databases/" + NAME;
        String backupDBPath = NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.i(TAG, "DB Exported!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAllTables(SQLiteDatabase db) {
        db.execSQL(SQL_QUERIES.CREATE_DICTIONARY_TABLE);
        db.execSQL(SQL_QUERIES.CREATE_TRANSLATIONS_TABLE);
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL(SQL_QUERIES.DROP_DICTIONARY_TABLE);
        db.execSQL(SQL_QUERIES.DROP_TRANSLATIONS_TABLE);
    }
}
