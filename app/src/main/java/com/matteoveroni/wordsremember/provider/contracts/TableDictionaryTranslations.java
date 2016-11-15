package com.matteoveroni.wordsremember.provider.contracts;

import android.database.sqlite.SQLiteDatabase;

public class TableDictionaryTranslations {

    public static final String NAME = "dictionary_translations";

    public static final String COLUMN_DICTIONARY_WORD_ID = "dictionary_word_id";
    public static final String COLUMN_TRANSLATION_WORD_ID = "translation_word_id";

    public static final String[] ALL_COLUMNS =
            {
                    COLUMN_DICTIONARY_WORD_ID,
                    COLUMN_TRANSLATION_WORD_ID
            };

    private static class Queries {
        private static final String CREATE_TABLE = "create table "
                + NAME + " ( "
                + COLUMN_DICTIONARY_WORD_ID + " integer primary key, "
                + COLUMN_TRANSLATION_WORD_ID + " integer primary key"
                + " );";

        private static final String DROP_TABLE = "drop table if exists " + NAME;
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(Queries.CREATE_TABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL(Queries.DROP_TABLE);
    }
}
