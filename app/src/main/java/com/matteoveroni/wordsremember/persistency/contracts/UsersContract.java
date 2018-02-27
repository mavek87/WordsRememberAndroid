package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.user_profiles.UserProfilesProvider;

/**
 * @author Matteo Veroni
 */

public class UsersContract {

    public static final String NAME = UsersContract.Schema.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse(
            UserProfilesProvider.SCHEME + UserProfilesProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final String CONTENT_ITEM_MYME_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_MYME_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "Users";

        public static final String COL_ID = _ID;
        public static final String COL_USERNAME = "Username";
        public static final String COL_EMAIL = "Email";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_USERNAME = TABLE_NAME + "." + COL_USERNAME;
        public static final String TABLE_DOT_COL_EMAIL = TABLE_NAME + "." + COL_EMAIL;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_USERNAME,
                        TABLE_DOT_COL_EMAIL
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Schema.TABLE_NAME
                + "("
                + Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Schema.COL_USERNAME + " TEXT NOT NULL, "
                + Schema.COL_EMAIL + " TEXT NOT NULL, "
                + "CONSTRAINT UQ_Username UNIQUE (" + Schema.COL_USERNAME + ")"
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schema.TABLE_NAME;
    }
}
