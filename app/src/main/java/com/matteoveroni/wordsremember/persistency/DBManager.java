package com.matteoveroni.wordsremember.persistency;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.dao.UserDAO;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.persistency.dbhelpers.AbstractDBHelper;
import com.matteoveroni.wordsremember.persistency.dbhelpers.UserDBHelper;
import com.matteoveroni.wordsremember.persistency.dbhelpers.UserProfileDBHelper;
import com.matteoveroni.wordsremember.persistency.exceptions.DuplicatedUsernameException;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.users.User;

import lombok.Getter;

/**
 * @author Matteo Veroni
 */

public class DBManager {
    private static final int DB_VERSION = 1;

    private volatile static DBManager DB_MANAGER_UNIQUE_INSTANCE;

    private final Context context;

    private final UserDAO userDAO;
    private final UserProfilesDAO userProfilesDAO;

    @Getter
    private UserDBHelper userDBHelper;
    @Getter
    private UserProfileDBHelper userProfileDBHelper;

    private DBManager(Context context) {
        this.context = context;
        this.userDAO = new UserDAO(context, this);
        this.userProfilesDAO = new UserProfilesDAO(context, this);
    }

    public static synchronized DBManager getInstance(Context appContext) {
        if (DB_MANAGER_UNIQUE_INSTANCE == null) {
            DB_MANAGER_UNIQUE_INSTANCE = new DBManager(appContext);
        }
        return DB_MANAGER_UNIQUE_INSTANCE;
    }

    public AbstractDBHelper setupUserDBHelper(User user) {
        userDBHelper = new UserDBHelper(context, "user_" + user.getId() + "_profiles.db", DB_VERSION);

        if (!userDAO.isUserWithSameNameSaved(user.getUsername())) {
            try {
                userDAO.saveUser(user);
            } catch (DuplicatedUsernameException e) {
                throw new RuntimeException("Unexpected username duplication");
            }
        }

        return userDBHelper;
    }

    public AbstractDBHelper setupUserProfileDBHelper(Profile profile) {
        final String userProfileDbName = "profile_" + profile.getId() + "_of_user_" + profile.getUser().getId() + ".db";
        userProfileDBHelper = new UserProfileDBHelper(context, userProfileDbName, DB_VERSION);

        try {
            userProfilesDAO.saveUserProfile(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userProfileDBHelper;
    }

    @Deprecated
    public void deleteCurrentUserDB() throws Exception {
        // TODO: user profiles dbs for this user should be removed too.
        userDBHelper.deleteDatabase();
    }
}
