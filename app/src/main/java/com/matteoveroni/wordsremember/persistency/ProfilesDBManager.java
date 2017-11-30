package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Veroni
 */

public class ProfilesDBManager {

    public static final String TAG = TagGenerator.tag(ProfilesDBManager.class);
    private static final int DB_VERSION = 1;

    private final Map<UserProfile, DBHelper> dbHelpers = new HashMap<>();
    private final Context context;
    private UserProfile userProfileInUse;

    private volatile static ProfilesDBManager DB_MANAGER_UNIQUE_INSTANCE;

    private ProfilesDBManager(Context context) {
        this.context = context;
        setUserProfileInUse(UserProfile.SYSTEM_PROFILE);
    }

    public static ProfilesDBManager getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            synchronized (DBHelper.class) {
                if (DB_MANAGER_UNIQUE_INSTANCE == null) {
                    DB_MANAGER_UNIQUE_INSTANCE = new ProfilesDBManager(appContext);
                }
            }
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public void setUserProfileInUse(UserProfile userProfile) {
        userProfileInUse = userProfile;
        loadUserProfileDBHelper(userProfileInUse);
    }

    public DBHelper loadUserProfileDBHelper(UserProfile userProfile) {
        if (!dbHelpers.containsKey(userProfile)) {
            final DBHelper dbHelper = new DBHelper(context, userProfile, DB_VERSION);

            // create the db on disk if it doesn't exist
//            dbHelper.getReadableDatabase();
//            dbHelper.close();

            dbHelpers.put(userProfile, dbHelper);
            Log.d(TAG, "loaded dbHelper for user profile => " + userProfile.getName());
        }
        return dbHelpers.get(userProfile);
    }

    public void updateDBForNewUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) throws Exception {
        final DBHelper dbHelper = loadUserProfileDBHelper(oldUserProfile);
        if (dbHelper.isDatabaseCreated())
            dbHelper.renameDbForProfile(newUserProfile);

        if (userProfileInUse == oldUserProfile) {
            userProfileInUse = newUserProfile;
        }
        dbHelpers.remove(oldUserProfile);
        dbHelpers.put(newUserProfile, dbHelper);
    }

    public void deleteUserProfileDB(UserProfile userProfile) throws Exception {
        if (userProfile.isInvalidProfile())
            throw new IllegalArgumentException("Invalid user profile passed to deleteUserProfileDB");

        final DBHelper dbHelper = loadUserProfileDBHelper(userProfile);
        if (dbHelper.isDatabaseCreated())
            dbHelper.deleteDatabase();

        dbHelpers.remove(userProfile);
    }

    public SQLiteDatabase getReadableDBForCurrentProfile() {
        return dbHelpers.get(userProfileInUse).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDBForCurrentProfile() {
        return dbHelpers.get(userProfileInUse).getWritableDatabase();
    }

    public DBHelper getCurrentProfileDBHelper() {
        return dbHelpers.get(userProfileInUse);
    }

    public String getCurrentProfileDBName() {
        return dbHelpers.get(userProfileInUse).getDatabaseName();
    }

    public void deleteCurrentUserDB() throws Exception {
        dbHelpers.get(userProfileInUse).deleteDatabase();
    }

    public void resetCurrentProfileDB() {
        dbHelpers.get(userProfileInUse).resetDatabase();
    }

    public void closeCurrentProfileDB() {
        dbHelpers.get(userProfileInUse).close();
    }
}
