package com.matteoveroni.wordsremember.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Singleton class that manages SQLite Database creation, reset, export and upgrade operations
 * and which contains db attributes
 *
 * @author Matteo Veroni
 */
public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseManager";
    public static final String NAME = "wordsremember.db";
    public static final int VERSION = 1;

    private static final class SQL_QUERIES {
        private static final String CREATE_DICTIONARY_TABLE = "CREATE TABLE IF NOT EXISTS "
                + DictionaryContract.Schema.TABLE_NAME + " ( "
                + DictionaryContract.Schema.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DictionaryContract.Schema.COLUMN_NAME + " TEXT NOT NULL"
                + " );";

        private static final String DROP_DICTIONARY_TABLE = "DROP TABLE IF EXISTS " + DictionaryContract.Schema.TABLE_NAME;

        private static final String CREATE_TRANSLATIONS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + DictionaryContract.Schema.TABLE_NAME + " ( "
                + DictionaryContract.Schema.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DictionaryContract.Schema.COLUMN_NAME + " TEXT NOT NULL"
                + " );";

        private static final String DROP_TRANSLATIONS_TABLE = "DROP TABLE IF EXISTS " + DictionaryContract.Schema.TABLE_NAME;
    }

    /**
     * Unique DatabaseManager instance
     */
    private volatile static DatabaseManager DB_MANAGER;

    /**
     * DatabaseManager private constructor
     *
     * @param context
     */
    private DatabaseManager(Context context) {
        super(context, DatabaseManager.NAME, null, DatabaseManager.VERSION);
    }

    /**
     * Get or create a unique DatabaseManager instance
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
     * Method that drops all the tables and their content and recreate them
     */
    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
    }

    /**
     * Method that exports the database to the SD (/storage/emulated/legacy/)
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
