package com.matteoveroni.wordsremember.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import java.util.Arrays;
import java.util.HashSet;

/**
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#tutorial-sqlite-custom-contentprovider-and-loader
 */

public class DictionaryProvider extends ContentProvider {

    public static final String AUTHORITY = "com.matteoveroni.wordsremember.provider";

    public static final String BASE_PATH = "dictionary";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "dictionary";

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vocable";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int DICTIONARY = 10;
    private static final int VOCABLE = 20;

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, DICTIONARY);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", VOCABLE);
    }

    private DatabaseManager databaseManager;

    @Override
    public boolean onCreate() {
        databaseManager = DatabaseManager.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(DictionaryContract.Schema.TABLE_NAME);

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case DICTIONARY:
                break;
            case VOCABLE:
                // adding the ID to the original query
                queryBuilder.appendWhere(DictionaryContract.Schema.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        SQLiteDatabase db = databaseManager.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(DictionaryContract.Schema.ALL_COLUMNS));

            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
