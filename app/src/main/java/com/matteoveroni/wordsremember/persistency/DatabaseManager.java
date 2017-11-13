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

public class DatabaseManager {

    public static final String TAG = TagGenerator.tag(DatabaseManager.class);
    private static final int DB_VERSION = 1;

    private final Map<UserProfile, DatabaseHelper> dbHelpers = new HashMap<>();
    private final Context context;
    private UserProfile currentUserProfile;

    private volatile static DatabaseManager DB_MANAGER_UNIQUE_INSTANCE;

    private DatabaseManager(Context context) {
        this.context = context;
        setCurrentUserProfileAndCreateDbHelper(UserProfile.SYSTEM_PROFILE);
    }

    public static DatabaseManager getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (DB_MANAGER_UNIQUE_INSTANCE == null) {
                    DB_MANAGER_UNIQUE_INSTANCE = new DatabaseManager(appContext);
                }
            }
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public void setCurrentUserProfileAndCreateDbHelper(UserProfile userProfile) {
        createDbHelperForUserProfileIfNotPresent(userProfile);
        this.currentUserProfile = userProfile;
    }

    private void createDbHelperForUserProfileIfNotPresent(UserProfile userProfile) {
        if (!dbHelpers.containsKey(userProfile)) {
            Log.d(TAG, "Created dbHelper for profile => " + userProfile.getName());
            dbHelpers.put(userProfile, new DatabaseHelper(context, userProfile, DB_VERSION));
        }
    }

    public SQLiteDatabase getReadableDatabase() {
        return dbHelpers.get(currentUserProfile).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return dbHelpers.get(currentUserProfile).getWritableDatabase();
    }

    public boolean updateDatabaseForNewUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) {
        createDbHelperForUserProfileIfNotPresent(oldUserProfile);
        DatabaseHelper dbHelper = dbHelpers.get(oldUserProfile);
        boolean wasDbRenamed = dbHelper.renameDatabaseForNewProfile(newUserProfile);
        if (wasDbRenamed) {
            if (oldUserProfile == currentUserProfile) {
                currentUserProfile = newUserProfile;
            }
            dbHelpers.remove(oldUserProfile);
            dbHelpers.put(newUserProfile, dbHelper);
            return true;
        }
        return false;
    }

    public DatabaseHelper getDatabaseHelperForCurrentProfile() {
        return dbHelpers.get(currentUserProfile);
    }

    public String getDatabaseName() {
        return dbHelpers.get(currentUserProfile).getDatabaseName();
    }

    public void deleteDatabase() {
        dbHelpers.get(currentUserProfile).deleteDatabase();
    }

    public void resetDatabase() {
        dbHelpers.get(currentUserProfile).resetDatabase();
    }

    public void close() {
        dbHelpers.get(currentUserProfile).close();
    }
}
