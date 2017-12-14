package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.dictionary.DictionaryProvider;

/**
 * Contract class for Dictionary
 *
 * @author Matteo Veroni
 */
public final class DatesContract {

    public static final String NAME = Schema.TABLE_NAME;
    public static final String CURRENT_DATE_SQLITE = "CURRENT_DATE";

    public static final Uri CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    // Mime type
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "dates";

        public static final String COL_ID = _ID;
        public static final String COL_DATE = "date";

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
