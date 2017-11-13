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
        Long id = userProfile.getId();
        if (userProfile.isInvalidProfile() || id == null || id < 0) return -1;

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
        if (newUserProfile.isInvalidProfile()) return 0;

        if (oldUserProfile.isInvalidProfile())
            return (saveUserProfile(newUserProfile) > 0) ? 1 : 0;

        boolean wasDbRenamedForNewUserProfile = DatabaseManager.getInstance(context).updateDatabaseForNewUserProfile(oldUserProfile, newUserProfile);
        if (wasDbRenamedForNewUserProfile) {
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

    public int removeUserProfile(UserProfile userProfile) {
        Long id = userProfile.getId();
        if (userProfile.isInvalidProfile() || id == null || id < 0) return -1;

        return contentResolver.delete(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(id)}
        );
    }

    private ContentValues userProfileToContentValues(UserProfile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getName());
        return values;
    }
}


