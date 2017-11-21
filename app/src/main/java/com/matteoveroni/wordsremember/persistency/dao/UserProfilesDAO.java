package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.DatabaseManager;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

/**
 * @author Matteo Veroni
 */

public class UserProfilesDAO {

    private final ContentResolver contentResolver;
    private final Context context;

    public UserProfilesDAO(Context context) {
        this.contentResolver = context.getContentResolver();
        this.context = context;
    }

    public long saveUserProfile(UserProfile userProfile) {
        long id = userProfile.getId();
        if (userProfile.getId() > 0) return -1;

        Uri uri = contentResolver.insert(
                UserProfilesContract.CONTENT_URI,
                userProfileToContentValues(userProfile)
        );

        if (uri != null) {
            String createdRowId = uri.getLastPathSegment();
            if (!createdRowId.isEmpty()) {
                id = Long.valueOf(createdRowId);
            }
        }
        return id;
    }

    public int updateUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) {
        if (newUserProfile.isInvalidProfile()) return -1;

        if (oldUserProfile.isInvalidProfile())
            return (saveUserProfile(newUserProfile) > 0) ? 1 : -1;

        boolean isDbRenamed = DatabaseManager.getInstance(context).updateDatabaseForNewUserProfile(oldUserProfile, newUserProfile);
        if (isDbRenamed) {

            return contentResolver.update(
                    UserProfilesContract.CONTENT_URI,
                    userProfileToContentValues(newUserProfile),
                    UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                    new String[]{Long.toString(oldUserProfile.getId())}
            );

        } else {
            throw new RuntimeException("Impossible to rename db from old user profile: " + oldUserProfile + " to new user profile: " + newUserProfile);
        }
    }

    public int deleteUserProfile(UserProfile userProfile) {
        Long id = userProfile.getId();
        if (userProfile.isInvalidProfile()) return -1;

        boolean isDbRemoved = DatabaseManager.getInstance(context).deleteDatabaseForUserProfile(userProfile);
        if (isDbRemoved) {

            return contentResolver.delete(
                    UserProfilesContract.CONTENT_URI,
                    UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                    new String[]{Long.toString(id)}
            );

        } else {
            throw new RuntimeException("Impossible to delete db for profile: " + userProfile);
        }
    }

    private ContentValues userProfileToContentValues(UserProfile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getName());
        return values;
    }
}


