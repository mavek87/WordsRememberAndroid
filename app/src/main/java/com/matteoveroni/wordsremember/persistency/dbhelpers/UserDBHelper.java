package com.matteoveroni.wordsremember.persistency.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.UsersContract;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class UserDBHelper extends DBHelper {

    public UserDBHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    void createAllTables(SQLiteDatabase db) {
        Log.d(TAG, "creating all db tables");

        Log.d(TAG, UsersContract.Query.CREATE_TABLE);
        db.execSQL(UsersContract.Query.CREATE_TABLE);

        Log.d(TAG, UserProfilesContract.Query.CREATE_TABLE);
        db.execSQL(UserProfilesContract.Query.CREATE_TABLE);

        Log.d(TAG, "*********************");
    }

    @Override
    void dropAllTables(SQLiteDatabase db) {
        Log.d(TAG, "removing all db tables");

        Log.d(TAG, UserProfilesContract.Query.DROP_TABLE);
        db.execSQL(UserProfilesContract.Query.DROP_TABLE);

        Log.d(TAG, "*********************");
    }
}
