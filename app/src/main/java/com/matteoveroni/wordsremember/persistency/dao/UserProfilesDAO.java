package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class UserProfilesDAO {

    private final ContentResolver contentResolver;
    private final DBManager dbManager;

    public UserProfilesDAO(Context context, DBManager dbManager) {
        this.contentResolver = context.getContentResolver();
        this.dbManager = dbManager;
    }

    public boolean isUserProfileWithSameNamePresent(String userProfileName) {
        final Cursor cursor = contentResolver.query(
                UserProfilesContract.CONTENT_URI,
                new String[]{UserProfilesContract.Schema.COL_PROFILE_NAME},
                UserProfilesContract.Schema.COL_PROFILE_NAME + "=?",
                new String[]{userProfileName},
                null);

        return ((cursor != null) && (cursor.getCount() > 0));
    }

    public Profile getUserProfileWithName(String userProfileName) {
        final Cursor cursor = contentResolver.query(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.ALL_COLUMNS,
                UserProfilesContract.Schema.COL_PROFILE_NAME + "=?",
                new String[]{userProfileName},
                null);

        if (cursor != null && cursor.moveToFirst()) { //make sure you got results, and move to first row
            long profileId =  cursor.getLong(cursor.getColumnIndex(UserProfilesContract.Schema.COL_ID));
            String profileName = cursor.getString(cursor.getColumnIndex(UserProfilesContract.Schema.COL_PROFILE_NAME));
            return new Profile(profileId, profileName);
        } else {
            return null;
        }
    }

//    public Profile getUserUserWithName(String userProfileName) {
//        final Cursor cursor = contentResolver.query(
//                UserProfilesContract.CONTENT_URI,
//                UserProfilesContract.Schema.ALL_COLUMNS,
//                UserProfilesContract.Schema.COL_PROFILE_NAME + "=?",
//                new String[]{userProfileName},
//                null);
//
//        if (cursor != null && cursor.moveToFirst()) { //make sure you got results, and move to first row
//            long profileId =  cursor.getLong(cursor.getColumnIndex(UserProfilesContract.Schema.COL_ID));
//            String profileName = cursor.getString(cursor.getColumnIndex(UserProfilesContract.Schema.COL_PROFILE_NAME));
//            return new Profile(profileId, profileName);
//        } else {
//            return null;
//        }
//    }
//
//    public long saveUser(User user) throws Exception {
//        long id = user.getId();
//
//
//
//    }

    public long saveUserProfile(Profile userProfile) throws Exception {
        long id = userProfile.getId();

//        if (id > 0)
//            throw new Exception("Impossible to save on a already saved user profile. Use the updateUserProfile method.");

        Profile oldUserProfile = getUserProfileWithName(userProfile.getName());
        if (oldUserProfile != null) {
            updateUserProfile(oldUserProfile, userProfile);
            return oldUserProfile.getId();
        }

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

    public int updateUserProfile(Profile oldUserProfile, Profile newUserProfile) throws Exception {
//        checkIfUserProfileIsValidOrThrowException(newUserProfile, new IllegalArgumentException("Invalid new user profile to use for the update"));

        // TODO: handle save instead of update if userProfile is not persisted yet and this method is called
//        if (oldUserProfile.isInvalidProfile()) {
//            try {
//                saveUserProfile(newUserProfile);
//                return 1;
//            } catch (Exception ex) {
//                throw new IllegalArgumentException("Invalid old user profile to update and impossible to save new user profile");
//            }
//        }

        if (newUserProfile.equals(oldUserProfile)) return 0;

        //TODO: FIx this
//        dbManager.updateDBForNewUserProfile(oldUserProfile, newUserProfile);

        return contentResolver.update(
                UserProfilesContract.CONTENT_URI,
                userProfileToContentValues(newUserProfile),
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(oldUserProfile.getId())}
        );
    }

    public void deleteUserProfile(Profile userProfile) throws Exception {
//        checkIfUserProfileIsValidOrThrowException(userProfile, new IllegalArgumentException("Invalid user profile to delete"));

        //TODO: FIx this
//        dbManager.deleteUserProfileDB(userProfile);

        contentResolver.delete(
                UserProfilesContract.CONTENT_URI,
                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{Long.toString(userProfile.getId())}
        );
    }

//    private void checkIfUserProfileIsValidOrThrowException(Profile userProfile, Exception ex) throws Exception {
//        if (userProfile.isInvalidProfile())
//            throw ex;
//        if
//    }

    private ContentValues userProfileToContentValues(Profile userProfile) {
        final ContentValues values = new ContentValues();
        values.put(UserProfilesContract.Schema.COL_PROFILE_NAME, userProfile.getName());
        return values;
    }
}


