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

public class DBUserManager {

    public static final String TAG = TagGenerator.tag(DBUserManager.class);
    private static final int DB_VERSION = 1;

    private final Map<UserProfile, DBHelper> dbHelpers = new HashMap<>();
    private final Context context;
    private UserProfile currentUserProfile;

    private volatile static DBUserManager DB_MANAGER_UNIQUE_INSTANCE;

    private DBUserManager(Context context) {
        this.context = context;
        setCurrentUserProfileAndLoadDBHelper(UserProfile.SYSTEM_PROFILE);
    }

    public static DBUserManager getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            synchronized (DBHelper.class) {
                if (DB_MANAGER_UNIQUE_INSTANCE == null) {
                    DB_MANAGER_UNIQUE_INSTANCE = new DBUserManager(appContext);
                }
            }
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public DBHelper setCurrentUserProfileAndLoadDBHelper(UserProfile userProfile) {
        setCurrentUserProfile(userProfile);
        return loadUserProfileDBHelper(userProfile);
    }

    private void setCurrentUserProfile(UserProfile userProfile) {
        this.currentUserProfile = userProfile;
    }

    private DBHelper loadUserProfileDBHelper(UserProfile userProfile) {
        if (!dbHelpers.containsKey(userProfile)) {
            Log.d(TAG, "dbHelper for profile => " + userProfile.getName() + " created!");
            dbHelpers.put(userProfile, new DBHelper(context, userProfile, DB_VERSION));
        }
        return dbHelpers.get(userProfile);
    }

    public SQLiteDatabase getReadableDB() {
        return dbHelpers.get(currentUserProfile).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDB() {
        return dbHelpers.get(currentUserProfile).getWritableDatabase();
    }

    public boolean updateDBForNewUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) {
        if (oldUserProfile.isInvalidProfile() || newUserProfile.isInvalidProfile() || newUserProfile.equals(oldUserProfile))
            return false;

        DBHelper dbHelper = loadUserProfileDBHelper(oldUserProfile);
        boolean wasDbRenamed = dbHelper.renameDbForProfile(newUserProfile);
        if (wasDbRenamed) {
            if (oldUserProfile == currentUserProfile) {
                setCurrentUserProfile(newUserProfile);
            }
            dbHelpers.remove(oldUserProfile);
            dbHelpers.put(newUserProfile, dbHelper);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUserProfileDB(UserProfile userProfile) {
        if (userProfile.isInvalidProfile()) return false;

        return loadUserProfileDBHelper(userProfile).deleteDatabase();
    }

    public DBHelper getDBHelperForCurrentProfile() {
        return dbHelpers.get(currentUserProfile);
    }

    public String getDBName() {
        return dbHelpers.get(currentUserProfile).getDatabaseName();
    }

    public void deleteDB() {
        dbHelpers.get(currentUserProfile).deleteDatabase();
    }

    public void resetDB() {
        dbHelpers.get(currentUserProfile).resetDatabase();
    }

    public void close() {
        dbHelpers.get(currentUserProfile).close();
    }
}
