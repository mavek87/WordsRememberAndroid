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

    private final Context context;
    private final UserProfile userProfile;
    private final String dbName;

    public DatabaseHelper(Context context, UserProfile userProfile, int version) {
        super(context, userProfile.getProfileName().concat(".db"), null, version);
        this.context = context;
        this.userProfile = userProfile;
        this.dbName = userProfile.getProfileName().concat(".db");
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
}
