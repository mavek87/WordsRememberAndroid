package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.contracts.UsersContract;
import com.matteoveroni.wordsremember.persistency.exceptions.DuplicatedUsernameException;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class UserDAO {

    private final ContentResolver contentResolver;

    public UserDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public long saveUser(User user) throws DuplicatedUsernameException {
        long id = user.getId();

        if (isUserWithSameNameSaved(user.getUsername()))
            throw new DuplicatedUsernameException();

        Uri uri = contentResolver.insert(
                UsersContract.CONTENT_URI,
                userToContentValues(user)
        );

        if (uri != null) {
            String createdRowId = uri.getLastPathSegment();
            if (!createdRowId.isEmpty()) {
                id = Long.valueOf(createdRowId);
            }
        }
        return id;
    }

    public boolean isUserWithSameNameSaved(String username) {
        final Cursor cursor = contentResolver.query(
                UsersContract.CONTENT_URI,
                new String[]{UsersContract.Schema.COL_USERNAME},
                UsersContract.Schema.COL_USERNAME + "=?",
                new String[]{username},
                null);

        return (cursor != null && cursor.getCount() > 0);
    }

    private ContentValues userToContentValues(User user) {
        final ContentValues values = new ContentValues();
        values.put(UsersContract.Schema.COL_ID, user.getId());
        values.put(UsersContract.Schema.COL_USERNAME, user.getUsername());
        values.put(UsersContract.Schema.COL_EMAIL, user.getEmail());
        return values;
    }
}


