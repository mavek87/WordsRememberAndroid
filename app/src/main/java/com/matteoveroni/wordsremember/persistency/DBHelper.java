package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import java.io.File;

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
    private UserProfile userProfile;
    private String dbName;
    private String dbPath;
    private String dbPathAndName;

    public DBHelper(Context context, UserProfile userProfile, int version) {
        super(context, getDbNameForUserProfile(userProfile), null, version);
        this.context = context;
        this.userProfile = userProfile;
        this.dbName = getDbNameForUserProfile(userProfile);
        this.dbPath = context.getDatabasePath(dbName).getParent();
        this.dbPathAndName = dbPath.concat(File.separator + dbName);
//        if (!isDatabaseCreated()) {
//            SQLiteDatabase db = getReadableDatabase();
//            createAllTables(db);
//            db.close();
//        }
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
                throw new Exception(TAG + " - Impossible to remove db for \'" + this.userProfile.getName() + "\' userProfile.");
            }
        }
    }

    public void renameDbForProfile(UserProfile newUserProfile) throws Exception {
        String newDbName = getDbNameForUserProfile(newUserProfile);

        if (dbName.equals(newDbName)) return;

        close();

        renameJournalDbFile(newDbName);

//        renameDbFileForProfile(newUserProfile, newDbName);

        if (isDatabaseCreated()) {
            final File oldDbFile = context.getDatabasePath(dbName);
            final File newDbFile = new File(dbPath, newDbName);
            oldDbFile.renameTo(newDbFile);
        }
    }

    private void renameJournalDbFile(String dbNameNewUserProfile) {
        final File oldJournalFile = new File(dbPathAndName + JOURNAL_EXTENSION);
        final File newJournalFile = new File(dbPath.concat(File.separator + dbNameNewUserProfile + JOURNAL_EXTENSION));
        try {
            oldJournalFile.renameTo(newJournalFile);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            oldJournalFile.delete();
        }
    }

//    private void renameDbFileForProfile(UserProfile newUserProfile, String newDbName) throws Exception {
//        dbName = newDbName;
//        userProfile = newUserProfile;
//        dbPathAndName = dbPath.concat(File.separator + newDbName);
//        Log.i(TAG, "super.dbName() = " + super.getDatabaseName());
//    }

    private void createAllTables(SQLiteDatabase db) {
        if (userProfile.equals(UserProfile.SYSTEM_PROFILE)) {
            Log.d(TAG, UserProfilesContract.Query.CREATE_TABLE);
            db.execSQL(UserProfilesContract.Query.CREATE_TABLE);

            Log.d(TAG, UserProfilesContract.Query.INSERT_DEFAULT_PROFILE);
            db.execSQL(UserProfilesContract.Query.INSERT_DEFAULT_PROFILE);
        } else {
            Log.d(TAG, VocablesContract.Query.CREATE_TABLE);
            db.execSQL(VocablesContract.Query.CREATE_TABLE);

            Log.d(TAG, TranslationsContract.Query.CREATE_TABLE);
            db.execSQL(TranslationsContract.Query.CREATE_TABLE);

            Log.d(TAG, VocablesTranslationsContract.Query.CREATE_TABLE);
            db.execSQL(VocablesTranslationsContract.Query.CREATE_TABLE);
        }
    }

    private void dropAllTables(SQLiteDatabase db) {
        if (userProfile.equals(UserProfile.SYSTEM_PROFILE)) {
            Log.d(TAG, UserProfilesContract.Query.DROP_TABLE);
            db.execSQL(UserProfilesContract.Query.DROP_TABLE);
        } else {
            Log.d(TAG, VocablesContract.Query.DROP_TABLE);
            db.execSQL(VocablesContract.Query.DROP_TABLE);

            Log.d(TAG, TranslationsContract.Query.DROP_TABLE);
            db.execSQL(TranslationsContract.Query.DROP_TABLE);

            Log.d(TAG, VocablesTranslationsContract.Query.DROP_TABLE);
            db.execSQL(VocablesTranslationsContract.Query.DROP_TABLE);
        }
    }

    private static String getDbNameForUserProfile(UserProfile userProfile) {
        return userProfile.getName().concat(DB_EXTENSION);
    }
}
