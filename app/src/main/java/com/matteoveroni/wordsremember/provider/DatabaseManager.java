package com.matteoveroni.wordsremember.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

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
    public static final String TAG = new TagGenerator().getTag(DatabaseManager.class);
    public static final String NAME = MyApp.NAME + ".db";
    public static final int VERSION = 1;

    /**
     * Unique DatabaseManager's instance
     */
    private volatile static DatabaseManager DB_INSTANCE;

    /**
     * Public constructor
     *
     * TODO: is public to allow tests
     *
     * @param context The application's context
     */
    public DatabaseManager(Context context) {
        super(context, DatabaseManager.NAME, null, DatabaseManager.VERSION);
    }

    /**
     * Get or create a unique DatabaseManager's instance
     *
     * @param context The application's context
     * @return Unique DatabaseManager instance
     */
    public static final DatabaseManager getInstance(Context context) {
        if (DB_INSTANCE == null) {
            synchronized (DatabaseManager.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = new DatabaseManager(context);
                }
            }
        }
        return DB_INSTANCE;
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
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + MyApp.AUTHORITY + "/databases/" + NAME;
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
        db.execSQL(DictionaryContract.Queries.CREATE_TABLE);
        db.execSQL(TranslationsContract.Queries.CREATE_TABLE);
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL(DictionaryContract.Queries.DROP_TABLE);
        db.execSQL(TranslationsContract.Queries.DROP_TABLE);
    }
}
