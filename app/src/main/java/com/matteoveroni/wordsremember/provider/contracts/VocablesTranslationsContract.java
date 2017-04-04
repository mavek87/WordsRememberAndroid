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

    public static final String VOCABLES_TRANSLATIONS = "vocables_translations";
    public static final String TRANSLATIONS_FOR_VOCABLE = "translations_for_vocable";
    public static final String NOT_TRANSLATION_FOR_VOCABLE = "not_translations_for_vocable";

    public static final Uri VOCABLES_TRANSLATIONS_CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + VOCABLES_TRANSLATIONS
    );

    public static final Uri TRANSLATIONS_FOR_VOCABLE_CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + TRANSLATIONS_FOR_VOCABLE
    );

    public static final Uri NOT_TRANSLATION_FOR_VOCABLE_CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NOT_TRANSLATION_FOR_VOCABLE
    );

    // Mime type
    public static final String VOCABLES_TRANSLATIONS_CONTENT_DIR_TYPE = VOCABLES_TRANSLATIONS_CONTENT_URI + ".dir";
    public static final String TRANSLATIONS_FOR_VOCABLE_CONTENT_DIR_TYPE = TRANSLATIONS_FOR_VOCABLE_CONTENT_URI + ".dir";
    public static final String NOT_TRANSLATION_FOR_VOCABLE_NAME_CONTENT_ITEM_TYPE = NOT_TRANSLATION_FOR_VOCABLE_CONTENT_URI + ".item";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = VOCABLES_TRANSLATIONS;

        public static final String COL_ID = _ID;
        public static final String COL_VOCABLE_ID = "vocable_id";
        public static final String COL_TRANSLATION_ID = "translation_id";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + _ID;
        public static final String TABLE_DOT_COL_VOCABLE_ID = TABLE_NAME + "." + COL_VOCABLE_ID;
        public static final String TABLE_DOT_COL_TRANSLATION_ID = TABLE_NAME + "." + COL_TRANSLATION_ID;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_VOCABLE_ID,
                        TABLE_DOT_COL_TRANSLATION_ID
                };
    }

    public static class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME
                + "("
                + Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COL_VOCABLE_ID + " INTEGER NOT NULL, "
                + Schema.COL_TRANSLATION_ID + " INTEGER NOT NULL, "
                + "CONSTRAINT UQ_VocID_TraID UNIQUE (" + Schema.COL_VOCABLE_ID + ", " + Schema.COL_TRANSLATION_ID + ")"
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
