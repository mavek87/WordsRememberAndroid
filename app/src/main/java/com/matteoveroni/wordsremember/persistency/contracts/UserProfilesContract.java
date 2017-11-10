package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.user_profiles.UserProfilesProvider;

/**
 * @author Matteo Veroni
 */

public class UserProfilesContract {

    public static final String NAME = UserProfilesContract.Schema.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse(
            UserProfilesProvider.SCHEME + UserProfilesProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    // Mime type
    public static final String CONTENT_ITEM_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "profiles";

        public static final String COL_ID = _ID;
        public static final String COL_PROFILE_NAME = "profile_name";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_PROFILE_NAME = TABLE_NAME + "." + COL_PROFILE_NAME;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_PROFILE_NAME
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + UserProfilesContract.Schema.TABLE_NAME
                + "("
                + UserProfilesContract.Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserProfilesContract.Schema.COL_PROFILE_NAME + " TEXT NOT NULL, "
                + "CONSTRAINT UQ_ProfileName UNIQUE (" + UserProfilesContract.Schema.COL_PROFILE_NAME + ")"
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + UserProfilesContract.Schema.TABLE_NAME;

        public static final String INSERT_DEFAULT_PROFILE = "INSERT INTO " + UserProfilesContract.Schema.TABLE_NAME
                + " (" + Schema.COL_PROFILE_NAME + ") VALUES ('Profile1');";
    }
}
