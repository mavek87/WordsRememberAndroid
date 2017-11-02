package com.matteoveroni.wordsremember.persistency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.matteoveroni.wordsremember.user_profile.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Veroni
 */

public class DatabaseManager {

    private static final int DB_VERSION = 1;

    private final Map<UserProfile, DatabaseHelper> dbHelpers = new HashMap<>();
    private final Context context;
    private UserProfile currentUserProfile;

    public DatabaseManager(Context context, UserProfile userProfile) {
        this.context = context;
        this.currentUserProfile = userProfile;
        dbHelpers.put(userProfile, new DatabaseHelper(context, userProfile, DB_VERSION));
    }

    public void setUserProfile(UserProfile userProfile) {
        if (!dbHelpers.containsKey(userProfile)) {
            dbHelpers.put(userProfile, new DatabaseHelper(context, userProfile, DB_VERSION));
        }
        this.currentUserProfile = userProfile;
    }

    public SQLiteDatabase getReadableDatabase() {
        return dbHelpers.get(currentUserProfile).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return dbHelpers.get(currentUserProfile).getWritableDatabase();
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
