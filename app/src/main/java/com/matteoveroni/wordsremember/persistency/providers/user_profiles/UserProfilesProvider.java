package com.matteoveroni.wordsremember.persistency.providers.user_profiles;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

/**
 * @author Matteo Veroni
 */

public class UserProfilesProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(UserProfilesProvider.class);

    public static final String CONTENT_AUTHORITY = UserProfilesProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int PROFILES = 1;
    private static final int PROFILE_ID = 2;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UserProfilesContract.NAME, PROFILES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UserProfilesContract.NAME + "/#", PROFILE_ID);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case PROFILES:
                return UserProfilesContract.CONTENT_DIR_MYME_TYPE;
            case PROFILE_ID:
                return UserProfilesContract.CONTENT_ITEM_MYME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String whereSelection, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserProfilesContract.Schema.TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                break;
            case PROFILE_ID:
                whereSelection = UserProfilesContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

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

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                long id = db.insertOrThrow(UserProfilesContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(UserProfilesContract.CONTENT_URI + "/" + id);
            case PROFILE_ID:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + uri + " for INSERT");
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
        }
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't uses a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                updatedRowsCounter = db.update(UserProfilesContract.Schema.TABLE_NAME, values, whereSelection, whereArgs);
                break;
            case PROFILE_ID:
                String id = uri.getLastPathSegment();
                String where = UserProfilesContract.Schema.COL_ID + "=" + id;
                if (Str.isNotNullOrEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                updatedRowsCounter = db.update(
                        UserProfilesContract.Schema.TABLE_NAME,
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

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't uses a placeholder (?)
    @Override
    public int delete(@NonNull Uri uri, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = profileDBManager.getWritableDBForCurrentProfile();
        int deletedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case PROFILES:
                deletedRowsCounter = db.delete(UserProfilesContract.Schema.TABLE_NAME, whereSelection, whereArgs);
                break;
            case PROFILE_ID:
                String profileId = uri.getLastPathSegment();
                String where = UserProfilesContract.Schema.COL_ID + "=" + profileId;
                if (Str.isNotNullOrEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                deletedRowsCounter = db.delete(
                        UserProfilesContract.Schema.TABLE_NAME,
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
