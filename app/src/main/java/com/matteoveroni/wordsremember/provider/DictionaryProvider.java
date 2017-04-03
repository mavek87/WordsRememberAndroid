package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * @author Matteo Veroni
 */

public class DictionaryProvider extends AbstractExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(DictionaryProvider.class);

    private DatabaseManager databaseManager;

    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = DictionaryProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int VOCABLES = 1;
    private static final int VOCABLE_ID = 2;
    private static final int TRANSLATIONS = 3;
    private static final int TRANSLATION_ID = 4;
    private static final int VOCABLES_TRANSLATIONS = 5;
    private static final int TRANSLATIONS_FOR_VOCABLES = 6;
    private static final int NOT_TRANSLATIONS_FOR_VOCABLE_ID = 7;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME, VOCABLES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME + "/#", VOCABLE_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME, TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME + "/#", TRANSLATION_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.VOCABLES_TRANSLATIONS, VOCABLES_TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE, TRANSLATIONS_FOR_VOCABLES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE_NAME + "/#", NOT_TRANSLATIONS_FOR_VOCABLE_ID);
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
            case VOCABLES_TRANSLATIONS:
                return VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_DIR_TYPE;
            case TRANSLATIONS_FOR_VOCABLES:
                return VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_DIR_TYPE;
            case NOT_TRANSLATIONS_FOR_VOCABLE_ID:
                return VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE_NAME_CONTENT_ITEM_TYPE;
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
    public Cursor query(Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        final String OPERATION = "QUERY";
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        final String[] projectionWithCount = new String[100];
//        System.arraycopy(projection, 0, projectionWithCount, 0, projection.length);
//        projectionWithCount[projectionWithCount.length] = ""
        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                queryBuilder.setTables(VocablesContract.Schema.TABLE_NAME);
                break;
            case VOCABLE_ID:
                queryBuilder.setTables(VocablesContract.Schema.TABLE_NAME);
                whereSelection = VocablesContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            case TRANSLATIONS:
                queryBuilder.setTables(TranslationsContract.Schema.TABLE_NAME);
                break;
            case TRANSLATION_ID:
                queryBuilder.setTables(TranslationsContract.Schema.TABLE_NAME);
                whereSelection = TranslationsContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            case VOCABLES_TRANSLATIONS:
                queryBuilder.setTables(VocablesTranslationsContract.Schema.TABLE_NAME);
                break;
            case TRANSLATIONS_FOR_VOCABLES:

                // SELECT translations._id, translations.translation
                // FROM translations LEFT JOIN vocables_translations
                // ON (translations._id=vocables_translations.translation_id)
                // WHERE vocables_translations.vocable_id=?;

                projection = TranslationsContract.Schema.ALL_COLUMNS;

                queryBuilder.setTables(
                        TranslationsContract.Schema.TABLE_NAME + " LEFT JOIN " + VocablesTranslationsContract.Schema.TABLE_NAME
                                + " ON ("
                                + TranslationsContract.Schema.TABLE_DOT_COL_ID
                                + "="
                                + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID
                                + ")"
                );

                if (whereSelection == null || whereSelection.trim().isEmpty()) {
                    whereSelection = VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=?";
                }
                if (whereArgs == null || whereArgs.length == 0) {
                    whereArgs = new String[]{uri.getLastPathSegment()};
                }
                break;
            case NOT_TRANSLATIONS_FOR_VOCABLE_ID:

                //   SELECT translations._id AS _id, translations.translation AS translation FROM translations
                //   WHERE NOT EXISTS (
                //      SELECT vocables_translations.translation_id FROM vocables_translations
                //      WHERE translations._id=vocables_translations.translation_id
                //   )
                //   UNION
                //   SELECT translations._id AS _id,translations.translation AS translation FROM translations
                //   INNER JOIN
                //   (
                //      SELECT * FROM vocables_translations WHERE vocables_translations.translation_id
                //      NOT IN (
                //          SELECT vocables_translations.translation_id FROM vocables_translations WHERE vocables_translations.vocable_id=3
                //	    )
                //   ) not_mine_translations
                //   ON (translations._id=not_mine_translations.translation_id)

                String Voc_Tra = VocablesTranslationsContract.Schema.TABLE_NAME;

                String SELECT_ALL_TRANSLATIONS = "SELECT "
                        + TranslationsContract.Schema.TABLE_DOT_COL_ID + " AS " + TranslationsContract.Schema.COL_ID + ", "
                        + TranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION + " AS " + TranslationsContract.Schema.COL_TRANSLATION + " "
                        + "FROM " + TranslationsContract.Schema.TABLE_NAME;

                String SELECT_VOCABLES_TRANSLATIONS_TRANSLATION_ID = "SELECT "
                        + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + " "
                        + "FROM " + Voc_Tra;

                String SQL_QUERY_SELECT_ALL_THE_TRANSLATIONS_NOT_ASSOCIATED_TO_ANY_VOCABLE = SELECT_ALL_TRANSLATIONS + " "
                        + "WHERE NOT EXISTS ("
                        + SELECT_VOCABLES_TRANSLATIONS_TRANSLATION_ID + " "
                        + "WHERE " + TranslationsContract.Schema.TABLE_DOT_COL_ID + "=" + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID
                        + ")";

                String SELECT_ALL_MY_TRANSLATIONS = SELECT_VOCABLES_TRANSLATIONS_TRANSLATION_ID + " "
                        + "WHERE " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=?";

                String SELECT_NOT_MINE_TRANSLATIONS = SELECT_VOCABLES_TRANSLATIONS_TRANSLATION_ID + " "
                        + "WHERE " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + " "
                        + "NOT IN (" + SELECT_ALL_MY_TRANSLATIONS + ")";

                String SQL_QUERY_ASSOCIATED_TRANSLATIONS_NOT_MINE = SELECT_ALL_TRANSLATIONS + " "
                        + "INNER JOIN (" + SELECT_NOT_MINE_TRANSLATIONS + ") not_mine_translations "
                        + "ON (" + TranslationsContract.Schema.TABLE_DOT_COL_ID + "=not_mine_translations." + VocablesTranslationsContract.Schema.COL_TRANSLATION_ID + ") ";

                ///////////////////////////////////////////////////////////////////////////////////////

                String SQL_QUERY_NOT_TRANSLATIONS_FOR_VOCABLE_ID = SQL_QUERY_SELECT_ALL_THE_TRANSLATIONS_NOT_ASSOCIATED_TO_ANY_VOCABLE
                        + " UNION "
                        + SQL_QUERY_ASSOCIATED_TRANSLATIONS_NOT_MINE;

                whereArgs = new String[]{uri.getLastPathSegment()};

                Cursor cursor = databaseManager.getReadableDatabase().rawQuery(SQL_QUERY_NOT_TRANSLATIONS_FOR_VOCABLE_ID, whereArgs);
                if (isContentResolverNotNull())
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri + " for " + OPERATION);
        }
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                whereSelection,
                whereArgs,
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
        final String OPERATION = "INSERT";
        final SQLiteDatabase db = databaseManager.getWritableDatabase();
        Uri contractUri;
        long id;

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                contractUri = VocablesContract.CONTENT_URI;
                id = db.insertOrThrow(VocablesContract.Schema.TABLE_NAME, null, values);
                break;
            case TRANSLATIONS:
                contractUri = TranslationsContract.CONTENT_URI;
                id = db.insertOrThrow(TranslationsContract.Schema.TABLE_NAME, null, values);
                break;
            case VOCABLES_TRANSLATIONS:
                contractUri = VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI;
                id = db.insertOrThrow(VocablesTranslationsContract.Schema.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri + " for " + OPERATION);
        }
        notifyChangeToObservers(uri);
        return Uri.parse(contractUri + "/" + id);
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final String OPERATION = "UPDATE";
        final SQLiteDatabase db = databaseManager.getWritableDatabase();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                updatedRowsCounter = db.update(VocablesContract.Schema.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                final String id = uri.getLastPathSegment();
                final String where = VocablesContract.Schema.COL_ID + " = " + id + (
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
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri + " for " + OPERATION);
        }
        notifyChangeToObservers(uri);
        return updatedRowsCounter;
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        final String OPERATION = "DELETE";
        final SQLiteDatabase db = databaseManager.getWritableDatabase();
        int deletedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                deletedRowsCounter = db.delete(VocablesContract.Schema.TABLE_NAME, whereClause, whereArgs);
                break;
            case VOCABLE_ID:
                String vocableId = uri.getLastPathSegment();
                String where = VocablesContract.Schema.COL_ID + "=" + vocableId;
                if (!TextUtils.isEmpty(whereClause)) {
                    where += " AND (" + whereClause + ")";
                }
                deletedRowsCounter = db.delete(
                        VocablesContract.Schema.TABLE_NAME,
                        where,
                        whereArgs
                );
                break;
            case VOCABLES_TRANSLATIONS:
                deletedRowsCounter = db.delete(VocablesTranslationsContract.Schema.TABLE_NAME, whereClause, whereArgs);
                break;
            default:
                throw new IllegalArgumentException(Errors.UNSUPPORTED_URI + uri + " for " + OPERATION);
        }
        notifyChangeToObservers(uri);
        return deletedRowsCounter;
    }

    private void notifyChangeToObservers(Uri uri) {
        if (isContentResolverNotNull())
            getContext().getContentResolver().notifyChange(uri, null);
    }

    private boolean isContentResolverNotNull() {
        Context ctx = getContext();
        return (ctx != null && ctx.getContentResolver() != null);
    }
}
