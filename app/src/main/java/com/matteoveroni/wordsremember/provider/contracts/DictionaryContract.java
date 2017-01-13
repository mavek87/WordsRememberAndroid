package com.matteoveroni.wordsremember.provider.contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

/**
 * Contract class that defines Dictionary Table Schema
 *
 * @author Matteo Veroni
 */
public final class DictionaryContract {

    public static final String NAME = Schema.TABLE_NAME;

    public static final Uri CONTENT_URI =
            Uri.parse(
                    DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
            );

    // Mime type
    public static final String CONTENT_TYPE_MULTIPLE = CONTENT_URI + "/vocables";
    public static final String CONTENT_TYPE_SINGLE = CONTENT_URI + "/vocable";

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
