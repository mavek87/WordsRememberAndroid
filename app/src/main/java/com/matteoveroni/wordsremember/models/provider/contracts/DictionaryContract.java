package com.matteoveroni.wordsremember.models.provider.contracts;

import android.provider.BaseColumns;

/**
 * Contract class that defines Dictionary Table Schema
 *
 * @author Matteo Veroni
 */
public final class DictionaryContract {

    private DictionaryContract() {
    }

    public static final class Schema implements BaseColumns {

        public static final String TABLE_NAME = "dictionary";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";

        public static final String[] ALL_COLUMNS =
                {
                        COLUMN_ID,
                        COLUMN_NAME
                };
    }
}
