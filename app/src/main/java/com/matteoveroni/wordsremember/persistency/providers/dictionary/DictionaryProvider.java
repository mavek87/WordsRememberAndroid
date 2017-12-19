package com.matteoveroni.wordsremember.persistency.providers.dictionary;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

/**
 * @author Matteo Veroni
 *         https://stackoverflow.com/questions/40481035/diference-between-cursor-setnotificationuri-and-getcontentresolver-notifycha
 */

public class DictionaryProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(DictionaryProvider.class);

    public static final String CONTENT_AUTHORITY = DictionaryProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int VOCABLES = 1;
    private static final int VOCABLE_ID = 2;
    private static final int TRANSLATIONS = 3;
    private static final int TRANSLATION_ID = 4;
    private static final int VOCABLES_TRANSLATIONS = 5;
    private static final int TRANSLATIONS_FOR_VOCABLE = 6;
    private static final int NOT_TRANSLATIONS_FOR_VOCABLE = 7;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME, VOCABLES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesContract.NAME + "/#", VOCABLE_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME, TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TranslationsContract.NAME + "/#", TRANSLATION_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.VOCABLES_TRANSLATIONS, VOCABLES_TRANSLATIONS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE, TRANSLATIONS_FOR_VOCABLE);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE + "/#", NOT_TRANSLATIONS_FOR_VOCABLE);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case VOCABLES:
                return VocablesContract.CONTENT_DIR_MYME_TYPE;
            case VOCABLE_ID:
                return VocablesContract.CONTENT_ITEM_MYME_TYPE;
            case TRANSLATIONS:
                return TranslationsContract.CONTENT_DIR_MYME_TYPE;
            case TRANSLATION_ID:
                return TranslationsContract.CONTENT_ITEM_MYME_TYPE;
            case VOCABLES_TRANSLATIONS:
                return VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_DIR_MYME_TYPE;
            case TRANSLATIONS_FOR_VOCABLE:
                return VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_DIR_MYME_TYPE;
            case NOT_TRANSLATIONS_FOR_VOCABLE:
                return VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE_NAME_CONTENT_ITEM_MYME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

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
            case TRANSLATIONS_FOR_VOCABLE:
                 /* SELECT translations._id, translations.translation
                 FROM translations LEFT JOIN vocables_translations
                 ON (translations._id=vocables_translations.translation_id)
                 WHERE vocables_translations.vocable_id=?; */

                projection = TranslationsContract.Schema.ALL_COLUMNS;

                queryBuilder.setTables(TranslationsContract.Schema.TABLE_NAME
                        + " LEFT JOIN " + VocablesTranslationsContract.Schema.TABLE_NAME
                        + " ON (" + TranslationsContract.Schema.TABLE_DOT_COL_ID + "=" + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + ")"
                );

                if (whereSelection == null || whereSelection.trim().isEmpty())
                    whereSelection = VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=?";

                if (whereArgs == null || whereArgs.length == 0)
                    whereArgs = new String[]{uri.getLastPathSegment()};

                break;
            case NOT_TRANSLATIONS_FOR_VOCABLE:

                   /* SELECT translations._id AS _id, translations.translation AS translation FROM translations
                   WHERE NOT EXISTS (
                      SELECT vocables_translations.translation_id FROM vocables_translations
                      WHERE translations._id=vocables_translations.translation_id
                   )
                   UNION
                   SELECT translations._id AS _id,translations.translation AS translation FROM translations
                   INNER JOIN
                   (
                      SELECT * FROM vocables_translations WHERE vocables_translations.translation_id
                      NOT IN (
                          SELECT vocables_translations.translation_id FROM vocables_translations WHERE vocables_translations.vocable_id=3
                	    )
                   ) not_mine_translations
                   ON (translations._id=not_mine_translations.translation_id) */

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

                Cursor cursor = profileDBManager.getReadableDBForCurrentProfile().rawQuery(SQL_QUERY_NOT_TRANSLATIONS_FOR_VOCABLE_ID, whereArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();

        Cursor cursor = queryBuilder.query(
                db,
                projection,
                whereSelection,
                whereArgs,
                null,
                null,
                sortOrder,
                getQueryParametersValuesForLimitAndOffset(uri)
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();

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
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(contractUri + "/" + id);
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't uses a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case VOCABLES:
                updatedRowsCounter = db.update(VocablesContract.Schema.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VOCABLE_ID:
                String id = uri.getLastPathSegment();
                String where = VocablesContract.Schema.COL_ID + "=" + id + (
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
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for UPDATE");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRowsCounter;
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't uses a placeholder (?)
    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
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
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for DELETE");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRowsCounter;
    }
}
