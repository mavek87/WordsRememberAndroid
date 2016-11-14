package com.matteoveroni.wordsremember.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class TableDictionary {

    public static final String NAME = "dictionary";

    public static final String COLUMN_WORD_ID = "word_id";
    public static final String COLUMN_WORD_NAME = "word_name";

    public static final String[] ALL_COLUMNS =
            {
                    COLUMN_WORD_ID,
                    COLUMN_WORD_NAME
            };

    private static class Queries {
        private static final String CREATE_TABLE = "create table "
                + NAME + " ( "
                + COLUMN_WORD_ID + " integer primary key autoincrement, "
                + COLUMN_WORD_NAME + " text not null"
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
