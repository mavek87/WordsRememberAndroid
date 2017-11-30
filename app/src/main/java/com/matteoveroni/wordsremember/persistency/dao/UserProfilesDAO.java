package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;
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

        ProfilesDBManager.getInstance(context).loadUserProfileDBHelper(userProfile);

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

    public int updateUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) throws Exception {
        if (newUserProfile.isInvalidProfile())
            throw new IllegalArgumentException("Invalid new user profile to use for the update");

        if (oldUserProfile.isInvalidProfile()) {
            long userProfileId = saveUserProfile(newUserProfile);
            if (userProfileId > 0)
                return 1;
            else
                throw new Exception("Invalid old user profile to update and Impossible to save new user profile");
        }

        if (newUserProfile.equals(oldUserProfile)) return 0;

        ProfilesDBManager.getInstance(context).updateDBForNewUserProfile(oldUserProfile, newUserProfile);

        return contentResolver.update(
                UserProfilesContract.CONTENT_URI,
                userProfileToContentValues(newUserProfile),
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(oldUserProfile.getId())}
        );
    }

    public void deleteUserProfile(UserProfile userProfile) throws Exception {
        final Long userId = userProfile.getId();

        if (userProfile.isInvalidProfile()) throw new Exception();

        ProfilesDBManager.getInstance(context).deleteUserProfileDB(userProfile);

        contentResolver.delete(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(userId)}
        );
    }

    private ContentValues userProfileToContentValues(UserProfile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getName());
        return values;
    }
}


