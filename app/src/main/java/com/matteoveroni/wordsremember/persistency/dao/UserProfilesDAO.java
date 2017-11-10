package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

/**
 * @author Matteo Veroni
 */

public class UserProfilesDAO {

    private final ContentResolver contentResolver;

    public UserProfilesDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public long saveUserProfile(UserProfile userProfile) {
        Long id = userProfile.getId();
        if (userProfile.hasNullOrEmptyName() || id == null || id < 0) return -1;

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

    public int updateUserProfile(long id, UserProfile userProfile) {
        if (userProfile.hasNullOrEmptyName() || id < 0) return -1;

        return contentResolver.update(
                UserProfilesContract.CONTENT_URI,
                userProfileToContentValues(userProfile),
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(id)}
        );
    }

    public int removeUserProfile(UserProfile userProfile) {
        Long id = userProfile.getId();
        if (userProfile.hasNullOrEmptyName() || id == null || id < 0) return -1;

        return contentResolver.delete(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(id)}
        );
    }

    private ContentValues userProfileToContentValues(UserProfile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getProfileName());
        return values;
    }
}


