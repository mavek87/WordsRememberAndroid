package com.matteoveroni.wordsremember.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Content Provider for the dictionary.
 *
 * @author Matteo Veroni
 */

/**
 * Useful resources on Content Providers:
 *
 * https://youtu.be/IWP2-qkhtiM?list=PLZ9NgFYEMxp50tvT8806xllaCbd31DpDy
 * https://github.com/margaretmz/andevcon/tree/master/SampleContentProvider/
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#tutorial-sqlite-custom-contentprovider-and-loader
 * http://stackoverflow.com/questions/11131058/how-to-properly-insert-values-into-the-sqlite-database-using-contentproviders-i
 * http://www.androiddesignpatterns.com/2012/06/content-resolvers-and-content-providers.html
 */

public class DictionaryProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = new TagGenerator().getTag(DictionaryProvider.class);

    // Database manager/helper singleton instance

    private DatabaseManager databaseManager;

    // Content Provider Parameters

    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.matteoveroni.wordsremember.provider";

    // Dictionary provider

    private static final int VOCABLES = 1;
    private static final int VOCABLE_ID = 2;

    // Translations provider

    public static final Uri TRANSLATIONS_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY + "/" + TranslationsContract.NAME);
    private static final int TRANSLATIONS = 3;
    private static final int TRANSLATION_ID = 4;

    // URI Matcher

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, DictionaryContract.NAME, VOCABLES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, DictionaryContract.NAME + "/#", VOCABLE_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME, TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME + "/#", TRANSLATION_ID);
    }

    // MIME type

    public static final String CONTENT_TYPE_MULTIPLE = ContentResolver.CURSOR_DIR_BASE_TYPE;
    public static final String CONTENT_TYPE_SINGLE = ContentResolver.CURSOR_ITEM_BASE_TYPE;

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case VOCABLES:
                return CONTENT_TYPE_MULTIPLE;
            case VOCABLE_ID:
                return CONTENT_TYPE_SINGLE;
            case TRANSLATIONS:
                return CONTENT_TYPE_MULTIPLE;
            case TRANSLATION_ID:
                return CONTENT_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        databaseManager = DatabaseManager.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        checkColumnsExistence(projection);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DictionaryContract.Schema.TABLE_NAME);

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case VOCABLES:
                break;
            case VOCABLE_ID:
                selection = DictionaryContract.Schema.COLUMN_ID + " = ? ";
                final String id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final SQLiteDatabase db = databaseManager.getWritableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                getQueryParameterLimitValue(uri)
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        final SQLiteDatabase db = databaseManager.getWritableDatabase();

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case VOCABLES:
                id = db.insertOrThrow(DictionaryContract.Schema.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DictionaryContract.NAME + "/" + id);
    }

    // TODO: this method is probably vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRowsCounter;
        final SQLiteDatabase db = databaseManager.getWritableDatabase();

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case VOCABLES:
                updatedRowsCounter = db.update(DictionaryContract.Schema.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                String id = uri.getLastPathSegment();
                updatedRowsCounter = db.update(
                        DictionaryContract.Schema.TABLE_NAME,
                        values,
                        DictionaryContract.Schema.COLUMN_ID + " =" + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), // append selection to query if selection is not empty
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRowsCounter;
    }

    /**
     * TODO: check VOCABLE_ITEM case not sure it works
     * TODO: this method is probably vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRowsCounter;
        final SQLiteDatabase db = databaseManager.getWritableDatabase();

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case VOCABLES:
                deletedRowsCounter = db.delete(DictionaryContract.Schema.TABLE_NAME, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                final String id = uri.getLastPathSegment();
                deletedRowsCounter =
                        db.delete(
                                DictionaryContract.Schema.TABLE_NAME,
                                DictionaryContract.Schema.COLUMN_ID
                                        + " = "
                                        + id
                                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), // append selection to query if selection is not empty
                                selectionArgs
                        );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRowsCounter;
    }

    private void checkColumnsExistence(String[] projection) {
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
