package com.matteoveroni.wordsremember.provider.contracts;

import android.provider.BaseColumns;

public final class TranslationsContract {

    private TranslationsContract() {}

    public static final class Schema implements BaseColumns {

        public static final String TABLE_NAME = "translations";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";

        public static final String[] ALL_COLUMNS =
                {
                        COLUMN_ID,
                        COLUMN_NAME
                };
    }
}
