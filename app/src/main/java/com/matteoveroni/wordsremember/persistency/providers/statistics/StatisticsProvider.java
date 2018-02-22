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
import com.matteoveroni.wordsremember.persistency.contracts.QuizStatsContract;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

public class StatisticsProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(StatisticsProvider.class);

    public static final String CONTENT_AUTHORITY = StatisticsProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int QUIZZES = 1;
    private static final int QUIZ_ID = 2;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, QuizStatsContract.NAME, QUIZZES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, QuizStatsContract.NAME + "/#", QUIZ_ID);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case QUIZZES:
                return QuizStatsContract.CONTENT_DIR_MYME_TYPE;
            case QUIZ_ID:
                return QuizStatsContract.CONTENT_ITEM_MYME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case QUIZZES:
                queryBuilder.setTables(QuizStatsContract.Schema.TABLE_NAME);
                break;
            case QUIZ_ID:
                queryBuilder.setTables(QuizStatsContract.Schema.TABLE_NAME);
                whereSelection = QuizStatsContract.Schema.COL_ID + "=?";
                break;
            default:
                throw new IllegalArgumentException(ExtendedQueriesContentProvider.Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

        whereArgs = new String[]{uri.getLastPathSegment()};

        SQLiteDatabase db = dbManager.getUserProfileDBHelper().getReadableDatabase();
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
        SQLiteDatabase db = dbManager.getUserProfileDBHelper().getWritableDatabase();

        Uri contractUri;
        long id;

        switch (URI_MATCHER.match(uri)) {
            case QUIZZES:
                contractUri = QuizStatsContract.CONTENT_URI;
                id = db.insertOrThrow(QuizStatsContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case QUIZ_ID:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + uri + " for INSERT");
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
        }

        return Uri.parse(contractUri + "/" + id);
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = dbManager.getUserProfileDBHelper().getWritableDatabase();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case QUIZ_ID:
                String id = uri.getLastPathSegment();
                updatedRowsCounter = db.update(
                        DatesContract.Schema.TABLE_NAME, values,
                        DatesContract.Schema.COL_ID + "=" + id, whereArgs
                );
                break;
            case QUIZZES:
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
        SQLiteDatabase db = dbManager.getUserProfileDBHelper().getWritableDatabase();
        int deletedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case QUIZ_ID:
                String id = uri.getLastPathSegment();
                deletedRowsCounter = db.delete(
                        UserProfilesContract.Schema.TABLE_NAME,
                        DatesContract.Schema.COL_ID + "=" + id, whereArgs
                );
                break;
            case QUIZZES:
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
