package com.matteoveroni.wordsremember.provider.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

/**
 * https://en.wikipedia.org/wiki/Associative_entity
 * https://www.youtube.com/watch?v=LDlzIYFbiys
 *
 * @author Matteo Veroni
 */

public class VocablesTranslationsContract {

    public static final String NAME = "vocables_translations";

    public static final Uri CONTENT_URI =
            Uri.parse(
                    DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
            );

    // Mime type
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = NAME;

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_VOCABLE_ID = "vocable_id";
        public static final String COLUMN_TRANSLATION_ID = "translation_id";

        public static final String TABLE_DOT_COLUMN_ID = TABLE_NAME + "." + _ID;
        public static final String TABLE_DOT_COLUMN_VOCABLE_ID = TABLE_NAME + "." + COLUMN_VOCABLE_ID;
        public static final String TABLE_DOT_COLUMN_TRANSLATION_ID = TABLE_NAME + "." + COLUMN_TRANSLATION_ID;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COLUMN_ID,
                        TABLE_DOT_COLUMN_VOCABLE_ID,
                        TABLE_DOT_COLUMN_TRANSLATION_ID
                };
    }

    public static class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME
                + "("
                + Schema.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COLUMN_VOCABLE_ID + " INTEGER NOT NULL, "
                + Schema.COLUMN_TRANSLATION_ID + " INTEGER NOT NULL, "
                + "CONSTRAINT UQ_VocID_TraID UNIQUE (" + Schema.COLUMN_VOCABLE_ID + ", " + Schema.COLUMN_TRANSLATION_ID + ")"
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
