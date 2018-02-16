package com.matteoveroni.wordsremember.persistency;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.dbhelpers.AbstractDBHelper;
import com.matteoveroni.wordsremember.persistency.dbhelpers.UserDBHelper;
import com.matteoveroni.wordsremember.persistency.dbhelpers.UserProfileDBHelper;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class DBManagerNew {

    private volatile static DBManagerNew DB_MANAGER_UNIQUE_INSTANCE;
    private static final int DB_VERSION = 1;

    private final Context context;
    private UserDBHelper userDBHelper;
    private UserProfileDBHelper userProfileDBHelper;

    private DBManagerNew(Context context) {
        this.context = context;
    }

    public static synchronized DBManagerNew getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            DB_MANAGER_UNIQUE_INSTANCE = new DBManagerNew(appContext);
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public AbstractDBHelper loadUserDBHelper(User user) {
        // TODO: verify and then remove this comment => This should load/create the db for the user
        return new UserDBHelper(context, "user_" + user.getId() + "_profiles", DB_VERSION);
    }

    public AbstractDBHelper loadUserProfileDBHelper(Profile profile) {
        final String USER_PROFILE_DB_NAME = userDBHelper.getDatabaseName() + "_" + profile.getId();
        userProfileDBHelper = new UserProfileDBHelper(context, USER_PROFILE_DB_NAME, DB_VERSION);
        return userDBHelper;
    }

    public void deleteCurrentUserDB() throws Exception {
        userDBHelper.deleteDatabase();
        // TODO: user profiles dbs for this user should be removed too.
    }
}
