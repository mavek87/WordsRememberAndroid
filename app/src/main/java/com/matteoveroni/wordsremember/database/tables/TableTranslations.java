package com.matteoveroni.wordsremember.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class TableTranslations {

    public static final String NAME = "translations";

    public static final String COLUMN_TRANSLATION_ID = "translation_id";
    public static final String COLUMN_TRANSLATION_NAME = "translation_name";

    public static final String[] ALL_COLUMNS =
            {
                    COLUMN_TRANSLATION_ID,
                    COLUMN_TRANSLATION_NAME
            };

    private static class Queries {
        private static final String CREATE_TABLE = "create table "
                + NAME + " ( "
                + COLUMN_TRANSLATION_ID + " integer primary key autoincrement, "
                + COLUMN_TRANSLATION_NAME + " text not null"
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
