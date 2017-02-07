package com.matteoveroni.wordsremember.provider.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

/**
 * Contract class for Dictionary
 *
 * @author Matteo Veroni
 */
public final class VocablesContract {

    public static final String NAME = Schema.TABLE_NAME;

    public static final Uri CONTENT_URI =
            Uri.parse(
                    DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
            );

    // Mime type
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

    private VocablesContract() {
    }

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "vocables";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_VOCABLE = "vocable";

        public static final String TABLE_DOT_COLUMN_ID = TABLE_NAME + "." + COLUMN_ID;
        public static final String TABLE_DOT_COLUMN_VOCABLE = TABLE_NAME + "." + COLUMN_VOCABLE;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COLUMN_ID,
                        TABLE_DOT_COLUMN_VOCABLE
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME
                + "("
                + Schema.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COLUMN_VOCABLE + " TEXT NOT NULL"
                + ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
