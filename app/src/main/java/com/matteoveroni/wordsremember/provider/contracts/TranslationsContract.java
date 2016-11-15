package com.matteoveroni.wordsremember.provider.contracts;

import android.provider.BaseColumns;

public class TranslationsContract {

    private TranslationsContract() {
    }

    public static final class TranslationsSchema implements BaseColumns {
        public static final String TABLE_NAME = "translations";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

        public static final String[] ALL_COLUMNS =
                {
                        COLUMN_ID,
                        COLUMN_NAME
                };
    }
}
