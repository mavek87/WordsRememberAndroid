package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.persistency.contracts.ProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

/**
 * Singleton helper class which contains db attributes and
 * manages SQLiteDatabase creation, init, export and upgrade operations
 *
 * How to see database in android from terminal:
 * http://stackoverflow.com/questions/17529766/view-contents-of-database-file-in-android-studio
 *
 * @author Matteo Veroni
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = TagGenerator.tag(DatabaseHelper.class);
    public static final String DB_NAME = WordsRemember.LOWERCASE_APP_NAME + ".db";
    public static final int VERSION = 1;

    private volatile static DatabaseHelper DB_UNIQUE_INSTANCE;

    private final Context context;

    private DatabaseHelper(Context context) {
        super(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.VERSION);
        this.context = context;
    }

    public static final DatabaseHelper getInstance(Context appContext) {
        if (DB_UNIQUE_INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (DB_UNIQUE_INSTANCE == null) {
                    DB_UNIQUE_INSTANCE = new DatabaseHelper(appContext);
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
        Log.d(TAG, VocablesContract.Query.CREATE_TABLE);
        db.execSQL(VocablesContract.Query.CREATE_TABLE);

        Log.d(TAG, TranslationsContract.Query.CREATE_TABLE);
        db.execSQL(TranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.CREATE_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, ProfilesContract.Query.CREATE_TABLE);
        db.execSQL(ProfilesContract.Query.CREATE_TABLE);

        Log.d(TAG, ProfilesContract.Query.INSERT_DEFAULT_PROFILE);
        db.execSQL(ProfilesContract.Query.INSERT_DEFAULT_PROFILE);
    }

    private void dropAllTables(SQLiteDatabase db) {
        Log.d(TAG, VocablesContract.Query.DROP_TABLE);
        db.execSQL(VocablesContract.Query.DROP_TABLE);

        Log.d(TAG, TranslationsContract.Query.DROP_TABLE);
        db.execSQL(TranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.DROP_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, ProfilesContract.Query.DROP_TABLE);
        db.execSQL(ProfilesContract.Query.DROP_TABLE);
    }
}
