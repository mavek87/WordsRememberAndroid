package com.matteoveroni.wordsremember.provider.contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

import static android.provider.ContactsContract.Settings.CONTENT_ITEM_TYPE;

/**
 * Contract class for Dictionary
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
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

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

    public static final class Queries {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME + " ( "
                + Schema.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COLUMN_NAME + " TEXT NOT NULL"
                + " );";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
