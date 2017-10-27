package com.matteoveroni.wordsremember.persistency.providers.profile;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.ProfilesContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

/**
 * @author Matteo Veroni
 */

public class ProfilesProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(ProfilesProvider.class);

    public static final String CONTENT_AUTHORITY = ProfilesProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int PROFILES = 1;
    private static final int PROFILE_ID = 2;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, ProfilesContract.NAME, PROFILES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, ProfilesContract.NAME + "/#", PROFILE_ID);
    }

    @Override
    public String getType(Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case PROFILES:
                return ProfilesContract.CONTENT_DIR_TYPE;
            case PROFILE_ID:
                return ProfilesContract.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(ProfilesContract.Schema.TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                break;
            case PROFILE_ID:
                whereSelection = ProfilesContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

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
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                Uri contractUri = ProfilesContract.CONTENT_URI;
                long id = db.insertOrThrow(ProfilesContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(contractUri + "/" + id);
            case PROFILE_ID:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + uri + " for INSERT");
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
        }

    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't use a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                updatedRowsCounter = db.update(ProfilesContract.Schema.TABLE_NAME, values, whereSelection, whereArgs);
                break;
            case PROFILE_ID:
                String id = uri.getLastPathSegment();
                String where = ProfilesContract.Schema.COL_ID + "=" + id;
                if (!TextUtils.isEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                updatedRowsCounter = db.update(
                        ProfilesContract.Schema.TABLE_NAME,
                        values,
                        where,
                        whereArgs
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
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int deletedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                deletedRowsCounter = db.delete(ProfilesContract.Schema.TABLE_NAME, whereSelection, whereArgs);
                break;
            case PROFILE_ID:
                String profileId = uri.getLastPathSegment();
                String where = ProfilesContract.Schema.COL_ID + "=" + profileId;
                if (!TextUtils.isEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                deletedRowsCounter = db.delete(
                        ProfilesContract.Schema.TABLE_NAME,
                        where,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for DELETE");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRowsCounter;
    }
}
