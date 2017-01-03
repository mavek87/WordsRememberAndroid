package com.matteoveroni.wordsremember.provider.contracts;

import android.provider.BaseColumns;

/**
 * Contract class that defines Translations Table Schema
 *
 * @author Matteo Veroni
 */
public final class TranslationsContract {

    private TranslationsContract() {
    }

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
}
