package com.matteoveroni.wordsremember.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Singleton helper class that manages SQLiteDatabase creation, reset, export and upgrade operations
 * and which contains db attributes.
 *
 * @author Matteo Veroni
 */

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TAG = TagGenerator.tag(DatabaseManager.class);
    public static final String DB_NAME = WordsRemember.LOWERCASE_NAME + ".db";
    public static final int VERSION = 1;

    private volatile static DatabaseManager DB_UNIQUE_INSTANCE;

    private DatabaseManager(Context context) {
        super(context, DatabaseManager.DB_NAME, null, DatabaseManager.VERSION);
    }

    public static final DatabaseManager getInstance(Context appContext) {
        if (DB_UNIQUE_INSTANCE == null) {
            synchronized (DatabaseManager.class) {
                if (DB_UNIQUE_INSTANCE == null) {
                    DB_UNIQUE_INSTANCE = new DatabaseManager(appContext);
                }
            }
        }
        return DB_UNIQUE_INSTANCE;
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

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
    }

    public void exportDatabaseOnSD() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + WordsRemember.AUTHORITY + "/databases/" + DB_NAME;
        String backupDBPath = DB_NAME;
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
            Log.d(TAG, "Error while exporting db on sd");
        }
    }

    private void createAllTables(SQLiteDatabase db) {
        db.execSQL(VocablesContract.Query.CREATE_TABLE);
        db.execSQL(TranslationsContract.Query.CREATE_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.CREATE_TABLE);
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL(VocablesContract.Query.DROP_TABLE);
        db.execSQL(TranslationsContract.Query.DROP_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.DROP_TABLE);
    }
}
