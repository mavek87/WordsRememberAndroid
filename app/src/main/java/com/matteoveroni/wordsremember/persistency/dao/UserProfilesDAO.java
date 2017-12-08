package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class UserProfilesDAO {

    private final ContentResolver contentResolver;
    private final ProfilesDBManager profilesDBManager;

    public UserProfilesDAO(Context context) {
        this.contentResolver = context.getContentResolver();
        this.profilesDBManager = ProfilesDBManager.getInstance(context);
    }

    public long saveUserProfile(UserProfile userProfile) throws Exception {
        if (userProfile.getId() > 0)
            throw new Exception("Impossible to save on a already saved user profile. Use the updateUserProfile method.");

        long id = userProfile.getId();

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
        checkIfUserProfileIsValidOrThrowException(newUserProfile, new IllegalArgumentException("Invalid new user profile to use for the update"));

        if (oldUserProfile.isInvalidProfile()) {
            try {
                saveUserProfile(newUserProfile);
                return 1;
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid old user profile to update and impossible to save new user profile");
            }
        }

        if (newUserProfile.equals(oldUserProfile)) return 0;

        profilesDBManager.updateDBForNewUserProfile(oldUserProfile, newUserProfile);

        return contentResolver.update(
                UserProfilesContract.CONTENT_URI,
                userProfileToContentValues(newUserProfile),
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(oldUserProfile.getId())}
        );
    }

    public void deleteUserProfile(UserProfile userProfile) throws Exception {
        checkIfUserProfileIsValidOrThrowException(userProfile, new IllegalArgumentException("Invalid user profile to delete"));

        profilesDBManager.deleteUserProfileDB(userProfile);

        contentResolver.delete(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(userProfile.getId())}
        );
    }

    private void checkIfUserProfileIsValidOrThrowException(UserProfile userProfile, Exception ex) throws Exception {
        if (userProfile.isInvalidProfile())
            throw ex;
    }

    private ContentValues userProfileToContentValues(UserProfile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getName());
        return values;
    }
}


