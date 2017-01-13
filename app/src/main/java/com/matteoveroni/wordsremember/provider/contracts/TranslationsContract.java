package com.matteoveroni.wordsremember.provider.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

import static com.matteoveroni.wordsremember.provider.DictionaryProvider.CONTENT_AUTHORITY;

/**
 * Contract class that defines Translations Table Schema
 *
 * @author Matteo Veroni
 */
public final class TranslationsContract {

    public static final String NAME = Schema.TABLE_NAME;

    public static final Uri CONTENT_URI =
            Uri.parse(
                    DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
            );

    // Mime type
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".dir";

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
