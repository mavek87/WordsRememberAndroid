package com.matteoveroni.wordsremember.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * Singleton helper class that manages SQLiteDatabase creation, init, export and upgrade operations
 * and which contains db attributes.
 *
 * @author Matteo Veroni
 */

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TAG = TagGenerator.tag(DatabaseManager.class);
    public static final String DB_NAME = WordsRemember.LOWERCASE_APP_NAME + ".db";
    public static final int VERSION = 1;

    private volatile static DatabaseManager DB_UNIQUE_INSTANCE;

    private final Context context;

    private DatabaseManager(Context context) {
        super(context, DatabaseManager.DB_NAME, null, DatabaseManager.VERSION);
        this.context = context;
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

    public void deleteDatabase() {
        context.deleteDatabase(DB_NAME);
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
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

    /**
     * How to see database in android from terminal
     *
     * http://stackoverflow.com/questions/17529766/view-contents-of-database-file-in-android-studio
     */
}
