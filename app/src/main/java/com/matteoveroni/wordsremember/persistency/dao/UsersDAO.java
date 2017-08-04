package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.persistency.contracts.UsersContract;
import com.matteoveroni.wordsremember.users.User;

/**
 * Class that allows CRUD operations on users data using a content resolver to communicate with
 * the users content provider.
 *
 * @author Matteo Veroni
 */

public class UsersDAO {

    private final ContentResolver contentResolver;

    public UsersDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    /***********************************************************************************************
     * Sync methods - Vocables
     **********************************************************************************************/

    public long saveUser(User user) {
        long id = user.getId();

        if (id < 1) return -1;

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

    /***********************************************************************************************
     * Helper methods
     **********************************************************************************************/

    public static User cursorToUser(Cursor cursor) {
        User user = new User(
                cursor.getString(cursor.getColumnIndex(UsersContract.Schema.COL_USERNAME)),
                cursor.getString(cursor.getColumnIndex(UsersContract.Schema.COL_EMAIL))
        );
        user.setId(cursor.getLong(cursor.getColumnIndex(UsersContract.Schema.COL_ID)));
        return user;
    }

//    public static List<Word> cursorToListOfTranslations(Cursor cursor) {
//        List<Word> translations = new ArrayList<>();
//        if (cursor == null) {
//            return translations;
//        }
//        while (cursor.moveToNext()) {
//            Word translation = new Word("");
//            translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));
//            translation.setName(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COL_TRANSLATION)));
//            translations.add(translation);
//        }
//        return translations;
//    }

    public ContentValues userToContentValues(User user) {
        ContentValues values = new ContentValues();

        values.put(UsersContract.Schema.COL_USERNAME, user.getUsername());
        values.put(UsersContract.Schema.COL_EMAIL, user.getEmail());

        return values;
    }
}


