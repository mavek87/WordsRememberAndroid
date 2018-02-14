package com.matteoveroni.wordsremember.persistency.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.matteoveroni.wordsremember.persistency.contracts.DatesContract;
import com.matteoveroni.wordsremember.persistency.contracts.QuizStatsContract;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

/**
 * @author Matteo Veroni
 */

public class DictionaryDBHelper extends AbstractDBHelper {

    public DictionaryDBHelper(Context context, String name, int version) {
        super(context, name, version);
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
