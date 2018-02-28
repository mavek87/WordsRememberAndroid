package com.matteoveroni.wordsremember.persistency.dbhelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.matteoveroni.wordsremember.persistency.contracts.DatesContract;
import com.matteoveroni.wordsremember.persistency.contracts.QuizStatsContract;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class UserProfileDBHelper extends DBHelper {

    public UserProfileDBHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    public void registerProfile(Profile profile) { //col, sel, args
        Cursor c = getWritableDatabase().query(
                UserProfilesContract.Schema.TABLE_NAME,
                new String[]{UserProfilesContract.Schema.COL_PROFILE_NAME},
                UserProfilesContract.Schema.COL_PROFILE_NAME + "=?",
                new String[]{profile.getName()},
                null,
                null,
                null,
                null
        );


    }

    @Override
    void createAllTables(SQLiteDatabase db) {
        Log.d(TAG, "creating all db tables");

        Log.d(TAG, VocablesContract.Query.CREATE_TABLE);
        db.execSQL(VocablesContract.Query.CREATE_TABLE);

        Log.d(TAG, TranslationsContract.Query.CREATE_TABLE);
        db.execSQL(TranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.CREATE_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.CREATE_TABLE);

        Log.d(TAG, QuizStatsContract.Query.CREATE_TABLE);
        db.execSQL(QuizStatsContract.Query.CREATE_TABLE);

        Log.d(TAG, DatesContract.Query.CREATE_TABLE);
        db.execSQL(DatesContract.Query.CREATE_TABLE);

        Log.d(TAG, "*********************");
    }

    @Override
    void dropAllTables(SQLiteDatabase db) {
        Log.d(TAG, "removing all db tables");

        Log.d(TAG, VocablesContract.Query.DROP_TABLE);
        db.execSQL(VocablesContract.Query.DROP_TABLE);

        Log.d(TAG, TranslationsContract.Query.DROP_TABLE);
        db.execSQL(TranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, VocablesTranslationsContract.Query.DROP_TABLE);
        db.execSQL(VocablesTranslationsContract.Query.DROP_TABLE);

        Log.d(TAG, QuizStatsContract.Query.DROP_TABLE);
        db.execSQL(QuizStatsContract.Query.DROP_TABLE);

        Log.d(TAG, DatesContract.Query.DROP_TABLE);
        db.execSQL(DatesContract.Query.DROP_TABLE);

        Log.d(TAG, "*********************");
    }
}
