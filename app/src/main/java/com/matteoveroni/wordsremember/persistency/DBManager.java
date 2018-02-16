package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.matteoveroni.wordsremember.persistency.dbhelpers.DBHelper;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Veroni
 */

public class DBManager {

    private static final int DB_VERSION = 1;

//    private final UserDBHelper userProfilesDBHelper;
    private final Map<Profile, DBHelper> dbHelpersMap = new HashMap<>();
    //todo: try to inject context
    private final Context context;
    private Profile userProfileInUse;

    private volatile static DBManager DB_MANAGER_UNIQUE_INSTANCE;

    private DBManager(Context context) {
        this.context = context;
        this.userProfileInUse = Profile.USER_PROFILES;
//        this.userProfilesDBHelper = new UserDBHelper(context, "user_profiles", 0);
    }

    public static synchronized DBManager getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            DB_MANAGER_UNIQUE_INSTANCE = new DBManager(appContext);
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public void setUserProfileInUse(Profile profile) {
        userProfileInUse = profile;
        createDBIfDoesntExist(loadUserProfileInDBHelpersMap(userProfileInUse));
    }

    private void createDBIfDoesntExist(DBHelper dbHelper) {
        dbHelper.getReadableDatabase().close();
    }

    private DBHelper loadUserProfileInDBHelpersMap(Profile profile) {
        if (!dbHelpersMap.containsKey(profile)) {
            final String dbName = (profile.equals(profile.USER_PROFILES)) ? profile.getName() : "" + profile.getId();
            dbHelpersMap.put(profile, new DBHelper(context, dbName, DB_VERSION));
        }

        return dbHelpersMap.get(profile);
    }

    public void updateDBForNewUserProfile(Profile oldUserProfile, Profile newUserProfile) throws Exception {
        loadUserProfileInDBHelpersMap(newUserProfile);
        dbHelpersMap.remove(oldUserProfile);
        if (userProfileInUse == oldUserProfile) userProfileInUse = newUserProfile;
    }

    public void deleteUserProfileDB(Profile userProfile) throws Exception {
        if (userProfile.isInvalidProfile())
            throw new IllegalArgumentException("Invalid user profile passed to deleteUserProfileDB");

        loadUserProfileInDBHelpersMap(userProfile).deleteDatabase();
        dbHelpersMap.remove(userProfile);
    }

    public SQLiteDatabase getReadableDBForCurrentProfile() {
        return dbHelpersMap.get(userProfileInUse).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDBForCurrentProfile() {
        return dbHelpersMap.get(userProfileInUse).getWritableDatabase();
    }

    public DBHelper getCurrentProfileDBHelper() {
        return dbHelpersMap.get(userProfileInUse);
    }

    public Profile getCurrentUserProfile() {
        return userProfileInUse;
    }

    public String getCurrentProfileDBName() {
        return dbHelpersMap.get(userProfileInUse).getDatabaseName();
    }

    public void deleteCurrentUserDB() throws Exception {
        dbHelpersMap.get(userProfileInUse).deleteDatabase();
    }

    public void resetCurrentProfileDB() {
        dbHelpersMap.get(userProfileInUse).resetDatabase();
    }

    public void closeCurrentProfileDB() {
        dbHelpersMap.get(userProfileInUse).close();
    }
}
