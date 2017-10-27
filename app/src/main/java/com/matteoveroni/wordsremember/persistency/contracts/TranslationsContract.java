package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.dictionary.DictionaryProvider;

/**
 * Contract class for Translations
 *
 * @author Matteo Veroni
 */
public final class TranslationsContract {

    public static final String NAME = Schema.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    // Mime type
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "translations";

        public static final String COL_ID = _ID;
        public static final String COL_TRANSLATION = "translation";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_TRANSLATION = TABLE_NAME + "." + COL_TRANSLATION;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_TRANSLATION
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME
                + "("
                + Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COL_TRANSLATION + " TEXT NOT NULL, "
                + "CONSTRAINT UQ_Translation UNIQUE (" + Schema.COL_TRANSLATION + ")"
                + ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
