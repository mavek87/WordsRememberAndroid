package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.dictionary.DictionaryProvider;
import com.matteoveroni.wordsremember.persistency.providers.statistics.StatisticsProvider;

/**
 * Contract class for Dictionary
 *
 * @author Matteo Veroni
 */
public final class DatesContract {

    public static final String NAME = Schema.TABLE_NAME;
    public static final String CURRENT_DATE_SQLITE = "CURRENT_DATE";

    public static final Uri DICTIONARY_CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final Uri STATISTICS_CONTENT_URI = Uri.parse(
            StatisticsProvider.SCHEME + StatisticsProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final String DICTIONARY_CONTENT_ITEM_MYME_TYPE = DICTIONARY_CONTENT_URI + ".item";
    public static final String DICTIONARY_CONTENT_DIR_MYME_TYPE= DICTIONARY_CONTENT_URI + ".dir";
    public static final String STATISTICS_CONTENT_ITEM_MYME_TYPE = STATISTICS_CONTENT_URI + ".item";
    public static final String STATISTICS_CONTENT_DIR_MYME_TYPE = STATISTICS_CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "Dates";

        public static final String COL_ID = _ID;
        public static final String COL_DATE = "Date";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_DATE = TABLE_NAME + "." + COL_DATE;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_DATE
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_NAME
                + "("
                + Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COL_DATE + " TEXT NOT NULL, "
                + "CONSTRAINT UQ_Date UNIQUE (" + Schema.COL_DATE + ")"
                + ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
