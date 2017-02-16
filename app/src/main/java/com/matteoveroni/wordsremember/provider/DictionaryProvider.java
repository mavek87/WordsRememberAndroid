package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

/**
 * Content Provider for the dictionary.
 *
 * @author Matteo Veroni
 */

/**
 * Useful resources on Content Providers:
 * <p>
 * https://youtu.be/IWP2-qkhtiM?list=PLZ9NgFYEMxp50tvT8806xllaCbd31DpDy
 * http://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/
 * https://github.com/margaretmz/andevcon/tree/master/SampleContentProvider/
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#tutorial-sqlite-custom-contentprovider-and-loader
 * http://stackoverflow.com/questions/11131058/how-to-properly-insert-values-into-the-sqlite-database-using-contentproviders-i
 * http://www.androiddesignpatterns.com/2012/06/content-resolvers-and-content-providers.html
 * sql inject => https://github.com/yahoo/squidb/wiki/Protecting-against-SQL-Injection
 * </p>
 */

public class DictionaryProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(DictionaryProvider.class);

    private DatabaseManager databaseManager;

    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = DictionaryProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int VOCABLES = 1;
    private static final int VOCABLE_ID = 2;
    private static final int TRANSLATIONS = 3;
    private static final int TRANSLATION_ID = 4;
    private static final int VOCABLE_TRANSLATIONS = 5;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME, VOCABLES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME + "/#", VOCABLE_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME, TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME + "/#", TRANSLATION_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.NAME, VOCABLE_TRANSLATIONS);
    }

    private static final class Errors {
        private static final String UNSUPPORTED_URI = "Unsupported URI ";
    }

    @Override
    public String getType(Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case VOCABLES:
                return VocablesContract.CONTENT_DIR_TYPE;
            case VOCABLE_ID:
                return VocablesContract.CONTENT_ITEM_TYPE;
            case TRANSLATIONS:
                return TranslationsContract.CONTENT_DIR_TYPE;
            case TRANSLATION_ID:
                return TranslationsContract.CONTENT_ITEM_TYPE;
            case VOCABLE_TRANSLATIONS:
                return VocablesTranslationsContract.CONTENT_DIR_TYPE;
            default:
                return null;
        }
    }

    @Override
    public boolean onCreate() {
        databaseManager = DatabaseManager.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                queryBuilder.setTables(VocablesContract.Schema.TABLE_NAME);
                break;
            case VOCABLE_ID:
                queryBuilder.setTables(VocablesContract.Schema.TABLE_NAME);
                selection = VocablesContract.Schema.COLUMN_ID + " = ? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case TRANSLATIONS:
                queryBuilder.setTables(TranslationsContract.Schema.TABLE_NAME);
                break;
            case TRANSLATION_ID:
                queryBuilder.setTables(TranslationsContract.Schema.TABLE_NAME);
                selection = TranslationsContract.Schema.COLUMN_ID + " = ? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case VOCABLE_TRANSLATIONS:
                //  SELECT translations.translation FROM vocables LEFT JOIN translations ON (vocables._id=translations._id) WHERE (vocables._id=1)
                projection = TranslationsContract.Schema.ALL_COLUMNS;
                queryBuilder.setTables(
                        VocablesContract.Schema.TABLE_NAME + " LEFT JOIN " + TranslationsContract.Schema.TABLE_NAME
                                + " ON ("
                                + VocablesContract.Schema.TABLE_DOT_COLUMN_ID
                                + " = "
                                + TranslationsContract.Schema.TABLE_DOT_COLUMN_ID
                                + ")"
                );
                selection = VocablesContract.Schema.TABLE_DOT_COLUMN_ID + " = ?";

                if (selectionArgs == null) {
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                }
                break;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri);
        }

        SQLiteDatabase db = databaseManager.getWritableDatabase();
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

        if (isContentResolverNotNull())
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                return InsertVocable(uri, values, db);
            case TRANSLATIONS:
                return InsertTranslation(uri, values, db);
            case VOCABLE_TRANSLATIONS:
                return InsertVocableTranslation(uri, values, db);
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri);
        }
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRowsCounter;
        final SQLiteDatabase db = databaseManager.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                updatedRowsCounter = db.update(VocablesContract.Schema.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                final String id = uri.getLastPathSegment();
                final String where = VocablesContract.Schema.COLUMN_ID + " = " + id + (
                        !TextUtils.isEmpty(selection)
                                ? " AND (" + selection + ")"
                                : ""
                );
                updatedRowsCounter = db.update(
                        VocablesContract.Schema.TABLE_NAME,
                        values,
                        where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri);
        }
        notifyChangeToObservers(uri);
        return updatedRowsCounter;
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRowsCounter;

        final SQLiteDatabase db = databaseManager.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                deletedRowsCounter = db.delete(VocablesContract.Schema.TABLE_NAME, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                final String id = uri.getLastPathSegment();
                final String where = VocablesContract.Schema.COLUMN_ID + " = " + id + (
                        !TextUtils.isEmpty(selection)
                                ? " AND (" + selection + ")"
                                : ""
                );
                deletedRowsCounter = db.delete(
                        VocablesContract.Schema.TABLE_NAME,
                        where,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri);
        }
        notifyChangeToObservers(uri);
        return deletedRowsCounter;
    }

//    private void checkColumnsExistence(String[] projection) {
//        if (projection != null) {
//            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
//            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(VocablesContract.Schema.ALL_COLUMNS));
//
//            // check if all columns which are requested are available
//            if (!availableColumns.containsAll(requestedColumns)) {
//                throw new IllegalArgumentException("Unknown columns in projection");
//            }
//        }
//    }

    private Uri InsertVocable(Uri uri, ContentValues values, SQLiteDatabase db) {
        long id = db.insertOrThrow(VocablesContract.Schema.TABLE_NAME, null, values);
        notifyChangeToObservers(uri);
        return Uri.parse(VocablesContract.CONTENT_URI + "/" + id);
    }

    private Uri InsertTranslation(Uri uri, ContentValues values, SQLiteDatabase db) {
        long id = db.insertOrThrow(TranslationsContract.Schema.TABLE_NAME, null, values);
        notifyChangeToObservers(uri);
        return Uri.parse(TranslationsContract.CONTENT_URI + "/" + id);
    }

    private Uri InsertVocableTranslation(Uri uri, ContentValues values, SQLiteDatabase db) {
        long id = db.insertOrThrow(VocablesTranslationsContract.Schema.TABLE_NAME, null, values);
        notifyChangeToObservers(uri);
        return Uri.parse(VocablesTranslationsContract.CONTENT_URI + "/" + id);
    }

    private void notifyChangeToObservers(Uri uri) {
        if (isContentResolverNotNull())
            getContext().getContentResolver().notifyChange(uri, null);
    }

    private boolean isContentResolverNotNull() {
        final Context ctx = getContext();
        return (ctx != null && ctx.getContentResolver() != null);
    }
}
