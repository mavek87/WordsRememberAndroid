package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import java.io.File;

/**
 * Singleton helper class which contains db attributes and
 * manages SQLiteDatabase creation, init, export and upgrade operations
 * <p>
 * How to see database in android from terminal:
 * http://stackoverflow.com/questions/17529766/view-contents-of-database-file-in-android-studio
 *
 * @author Matteo Veroni
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = TagGenerator.tag(DatabaseHelper.class);

    private static final String DB_EXTENSION = ".db";
    private static final String JOURNAL_EXTENSION = "-journal";

    private final Context context;
    private UserProfile userProfile;
    private String dbName;
    private String dbPath;

    public DatabaseHelper(Context context, UserProfile userProfile, int version) {
        super(context, getDbNameForUserProfile(userProfile), null, version);
        this.context = context;
        this.userProfile = userProfile;
        this.dbName = getDbNameForUserProfile(userProfile);
        this.dbPath = context.getDatabasePath(dbName).getParent();
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

    public void deleteDatabase() {
        context.deleteDatabase(dbName);
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dropAllTables(db);
        createAllTables(db);
        db.close();
    }

    public boolean renameDatabaseForNewProfile(UserProfile newUserProfile) {
        if (newUserProfile.isInvalidProfile() || dbName.equals(getDbNameForUserProfile(newUserProfile)))
            return false;

        close();

        renameJournalDbFile(newUserProfile);
        return renameDbFile(newUserProfile);
    }

    private void renameJournalDbFile(UserProfile newUserProfile) {
        final File oldJournalFile = new File(dbPath.concat(File.separator + userProfile.getName() + JOURNAL_EXTENSION));
        final File newJournalFile = new File(dbPath.concat(File.separator + newUserProfile.getName() + JOURNAL_EXTENSION));
        try {
            oldJournalFile.renameTo(newJournalFile);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private boolean renameDbFile(UserProfile newUserProfile) {
        final String newDbName = getDbNameForUserProfile(newUserProfile);

        final File oldDbFile = context.getDatabasePath(dbName);
        final File newDbFile = new File(oldDbFile.getParent(), newDbName);
        try {
            boolean isDbRenamed = oldDbFile.renameTo(newDbFile);
            if (isDbRenamed) {
                dbName = newDbName;
                userProfile = newUserProfile;
                dbPath = context.getDatabasePath(dbName).getParent();
            }
            return isDbRenamed;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }
    }

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
