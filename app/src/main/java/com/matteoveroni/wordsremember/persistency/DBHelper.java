package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.DatesContract;
import com.matteoveroni.wordsremember.persistency.contracts.QuizStatsContract;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

/**
 * Helper class which contains db attributes and
 * manages SQLiteDatabase creation, init, export and upgrade operations
 * <p>
 * How to see database in android from terminal:
 * http://stackoverflow.com/questions/17529766/view-contents-of-database-file-in-android-studio
 *
 * @author Matteo Veroni
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = TagGenerator.tag(DBHelper.class);

    private static final String DB_EXTENSION = ".db";
    private static final String JOURNAL_EXTENSION = "-journal";

    private final Context context;
    private String dbName;

    public DBHelper(Context context, String dbName, int version) {
        super(context, getDbNameWithExtension(dbName), null, version);
        this.context = context;
        this.dbName = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        if (dbName.equals(UserProfilesContract.Schema.TABLE_NAME)) {
//            createUserProfilesTable(db);
//        } else {
        createAllTables(db);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Updating db " + dbName + " from old version " + oldVersion + " to new version " + newVersion);
        dropAllTables(db);
        createAllTables(db);
    }

    public boolean isDatabaseCreated() {
        final boolean isDatabaseCreated = context.getDatabasePath(dbName).exists();
        Log.d(TAG, "isDatabaseCreated = " + isDatabaseCreated);
        return isDatabaseCreated;
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
    }

    public void deleteDatabase() throws Exception {
        if (isDatabaseCreated()) {
            boolean isDbDeleted = context.deleteDatabase(dbName);
            if (!isDbDeleted) {
                throw new Exception(TAG + " - Impossible to remove db \'" + dbName + "\'.");
            }
        }
    }

    private static String getDbNameWithExtension(String dbName) {
        return dbName + DB_EXTENSION;
    }

    private void createAllTables(SQLiteDatabase db) {
        Log.d(TAG, "creating all db tables");

        Log.d(TAG, UserProfilesContract.Query.CREATE_TABLE);
        db.execSQL(UserProfilesContract.Query.CREATE_TABLE);

        Log.d(TAG, VocablesContract.Query.CREATE_TABLE);
        db.execSQL(VocablesContract.Query.CREATE_TABLE);

        Log.d(TAG, TranslationsContract.Query.CREATE_TABLE);
        db.execSQL(TranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.CREATE_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, QuizStatsContract.Query.CREATE_TABLE);
        db.execSQL(QuizStatsContract.Query.CREATE_TABLE);

        Log.d(TAG, DatesContract.Query.CREATE_TABLE);
        db.execSQL(DatesContract.Query.CREATE_TABLE);

        Log.d(TAG, "*********************");
    }

    private void dropAllTables(SQLiteDatabase db) {
        Log.d(TAG, "removing all db tables");

        Log.d(TAG, UserProfilesContract.Query.DROP_TABLE);
        db.execSQL(UserProfilesContract.Query.DROP_TABLE);

        Log.d(TAG, VocablesContract.Query.DROP_TABLE);
        db.execSQL(VocablesContract.Query.DROP_TABLE);

        Log.d(TAG, TranslationsContract.Query.DROP_TABLE);
        db.execSQL(TranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.DROP_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, QuizStatsContract.Query.DROP_TABLE);
        db.execSQL(QuizStatsContract.Query.DROP_TABLE);

        Log.d(TAG, DatesContract.Query.DROP_TABLE);
        db.execSQL(DatesContract.Query.DROP_TABLE);

        Log.d(TAG, "*********************");
    }
}
