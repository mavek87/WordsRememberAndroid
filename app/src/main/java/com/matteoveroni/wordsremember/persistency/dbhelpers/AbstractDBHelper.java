package com.matteoveroni.wordsremember.persistency.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;

/**
 * @author Matteo Veroni
 */

public abstract class AbstractDBHelper extends SQLiteOpenHelper {

    public static final String TAG = TagGenerator.tag(AbstractDBHelper.class);

    private static final String DB_EXTENSION = ".db";
    private static final String JOURNAL_EXTENSION = "-journal";

    private final Context context;
    private String dbName;

    public AbstractDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context;
        this.dbName = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Updating db " + dbName + " from old version " + oldVersion + " to new version " + newVersion);
        dropAllTables(db);
        createAllTables(db);
    }

    public void createDatabaseIfItDoesntExist() {
        getReadableDatabase();
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

    public static String getDbNameWithExtension(String dbName) {
        return dbName + DB_EXTENSION;
    }

    abstract void createAllTables(SQLiteDatabase db);

    abstract void dropAllTables(SQLiteDatabase db);
}
