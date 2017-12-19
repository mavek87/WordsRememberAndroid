package com.matteoveroni.wordsremember.persistency.providers.statistics;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.DatesContract;
import com.matteoveroni.wordsremember.persistency.contracts.QuizzesStatsContract;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

public class StatisticsProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(StatisticsProvider.class);

    public static final String CONTENT_AUTHORITY = StatisticsProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int QUIZZES_STATS_ID = 1;
    private static final int DATE_ID = 10;
    private static final int DATE = 11;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, QuizzesStatsContract.NAME + "/#", QUIZZES_STATS_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, DatesContract.NAME + "/#", DATE_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, DatesContract.NAME + "/#", DATE);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case QUIZZES_STATS_ID:
                return QuizzesStatsContract.STATISTICS_MYME_TYPE_CONTENT_DIR;
            case DATE_ID:
                return DatesContract.STATISTICS_CONTENT_ITEM_MYME_TYPE;
            case DATE:
                return DatesContract.STATISTICS_CONTENT_ITEM_MYME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case DATE_ID:
                queryBuilder.setTables(DatesContract.Schema.TABLE_NAME);
                whereSelection = DatesContract.Schema.COL_ID + "=?";
                break;
            case DATE:
                queryBuilder.setTables(DatesContract.Schema.TABLE_NAME);
                whereSelection = DatesContract.Schema.COL_DATE + "=?";
                break;
            default:
                throw new IllegalArgumentException(ExtendedQueriesContentProvider.Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

        whereArgs = new String[]{uri.getLastPathSegment()};

        SQLiteDatabase db = profileDBManager.getReadableDBForCurrentProfile();
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
            case DATE:
                contractUri = DatesContract.STATISTICS_CONTENT_URI;
                id = db.insertOrThrow(DatesContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DATE_ID:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + uri + " for INSERT");
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
        }

        return Uri.parse(contractUri + "/" + id);
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case DATE_ID:
                String id = uri.getLastPathSegment();
                updatedRowsCounter = db.update(
                        DatesContract.Schema.TABLE_NAME, values,
                        DatesContract.Schema.COL_ID + "=" + id, whereArgs
                );
                break;
            case DATE:
                String date = uri.getLastPathSegment();
                updatedRowsCounter = db.update(
                        DatesContract.Schema.TABLE_NAME, values,
                        DatesContract.Schema.COL_DATE + "=" + date, whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for UPDATE");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRowsCounter;
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int delete(@NonNull Uri uri, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
        int deletedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case DATE_ID:
                String id = uri.getLastPathSegment();
                deletedRowsCounter = db.delete(
                        UserProfilesContract.Schema.TABLE_NAME,
                        DatesContract.Schema.COL_ID + "=" + id, whereArgs
                );
                break;
            case DATE:
                String date = uri.getLastPathSegment();
                deletedRowsCounter = db.delete(
                        UserProfilesContract.Schema.TABLE_NAME,
                        DatesContract.Schema.COL_DATE + "=" + date, whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for DELETE");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRowsCounter;
    }
}
