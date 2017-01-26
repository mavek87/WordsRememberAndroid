package com.matteoveroni.wordsremember.provider.contracts;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationsContract {

    public static final String NAME = "dictionary_translations";

    public static final String COLUMN_DICTIONARY_VOCABLE_ID = "vocable_id";
    public static final String COLUMN_TRANSLATIONS_TRANSLATION_ID = "translation_id";

    public static final class Schema implements BaseColumns {

        public static final String TABLE_NAME = "translations";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TRANSLATION = "translation";

        public static final String[] ALL_COLUMNS =
                {
                        COLUMN_ID,
                        COLUMN_TRANSLATION
                };
    }


//    public static final String[] ALL_COLUMNS =
//            {
//                    COLUMN_DICTIONARY_VOCABLE_ID,
//                    COLUMN_TRANSLATIONS_TRANSLATION_ID
//            };

//    private static class Queries {
//        private static final String CREATE_TABLE = "create table "
//                + NAME + " ( "
//                + COLUMN_DICTIONARY_VOCABLE_ID + " integer primary key, "
//                + COLUMN_TRANSLATIONS_TRANSLATION_ID + " integer primary key"
//                + " );";
//
//        private static final String DROP_TABLE = "drop table if exists " + NAME;
//    }

//    public static void createTable(SQLiteDatabase db) {
//        db.execSQL(Queries.CREATE_TABLE);
//    }
//
//    public static void dropTable(SQLiteDatabase db) {
//        db.execSQL(Queries.DROP_TABLE);
//    }
}
