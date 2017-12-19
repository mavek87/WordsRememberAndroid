package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Veroni
 */

public class ProfilesDBManager {

    private static final int DB_VERSION = 1;

    private final Map<UserProfile, DBHelper> dbHelpersMap = new HashMap<>();
    //todo: try to inject context
    private final Context context;
    private UserProfile userProfileInUse;

    private volatile static ProfilesDBManager DB_MANAGER_UNIQUE_INSTANCE;

    private ProfilesDBManager(Context context) {
        this.context = context;
        this.userProfileInUse = UserProfile.USER_PROFILES;
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
        createDBIfDoesntExist(loadUserProfileInDBHelpersMap(userProfileInUse));
    }

    private void createDBIfDoesntExist(DBHelper dbHelper) {
        dbHelper.getReadableDatabase().close();
    }

    private DBHelper loadUserProfileInDBHelpersMap(UserProfile userProfile) {
        if (!dbHelpersMap.containsKey(userProfile)) {
            final String dbName = (userProfile.equals(UserProfile.USER_PROFILES)) ? userProfile.getName() : "" + userProfile.getId();
            dbHelpersMap.put(userProfile, new DBHelper(context, dbName, DB_VERSION));
        }

        return dbHelpersMap.get(userProfile);
    }

    public void updateDBForNewUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) throws Exception {
        loadUserProfileInDBHelpersMap(newUserProfile);
        dbHelpersMap.remove(oldUserProfile);
        if (userProfileInUse == oldUserProfile) userProfileInUse = newUserProfile;
    }

    public void deleteUserProfileDB(UserProfile userProfile) throws Exception {
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
