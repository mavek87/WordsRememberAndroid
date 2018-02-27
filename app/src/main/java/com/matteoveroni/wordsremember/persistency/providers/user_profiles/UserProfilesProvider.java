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
import com.matteoveroni.wordsremember.persistency.contracts.UsersContract;
import com.matteoveroni.wordsremember.persistency.providers.ExtendedQueriesContentProvider;

/**
 * @author Matteo Veroni
 */

public class UserProfilesProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(UserProfilesProvider.class);

    public static final String CONTENT_AUTHORITY = UserProfilesProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int USERS = 1;
    private static final int USER_ID = 2;
    private static final int PROFILES = 3;
    private static final int PROFILE_ID = 4;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UsersContract.NAME, USERS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UsersContract.NAME + "/#", USER_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UserProfilesContract.NAME, PROFILES);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UserProfilesContract.NAME + "/#", PROFILE_ID);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch ((URI_MATCHER.match(uri))) {
            case USERS:
                return UsersContract.CONTENT_DIR_MYME_TYPE;
            case USER_ID:
                return UsersContract.CONTENT_ITEM_MYME_TYPE;
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

        switch (URI_MATCHER.match(uri)) {
            case USERS:
                queryBuilder.setTables(UsersContract.Schema.TABLE_NAME);
                break;
            case USER_ID:
                queryBuilder.setTables(UsersContract.Schema.TABLE_NAME);
                whereSelection = UsersContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            case PROFILES:
                queryBuilder.setTables(UserProfilesContract.Schema.TABLE_NAME);
                break;
            case PROFILE_ID:
                queryBuilder.setTables(UserProfilesContract.Schema.TABLE_NAME);
                whereSelection = UserProfilesContract.Schema.COL_ID + "=?";
                whereArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for QUERY");
        }

        SQLiteDatabase db = dbManager.getUserDBHelper().getReadableDatabase();
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
        SQLiteDatabase db = dbManager.getUserDBHelper().getWritableDatabase();
        long id;
        switch (URI_MATCHER.match(uri)) {
            case USERS:
                id = db.insertOrThrow(UsersContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(UsersContract.CONTENT_URI + "/" + id);
            case USER_ID:
                break;
            case PROFILES:
                id = db.insertOrThrow(UserProfilesContract.Schema.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(UserProfilesContract.CONTENT_URI + "/" + id);
            case PROFILE_ID:
                break;
            default:
                break;
        }
        throw new IllegalArgumentException(Error.UNSUPPORTED_URI + " " + uri + " for INSERT");
    }

    // TODO: this method is vulnerable to SQL inject attacks. It doesn't uses a placeholder (?)
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereSelection, String[] whereArgs) {
        SQLiteDatabase db = dbManager.getUserDBHelper().getWritableDatabase();
        String id;
        String where;
        int updatedRowsCounter;

        switch (URI_MATCHER.match(uri)) {
            case USERS:
                updatedRowsCounter = db.update(UsersContract.Schema.TABLE_NAME, values, whereSelection, whereArgs);
                break;
            case USER_ID:
                id = uri.getLastPathSegment();
                where = UserProfilesContract.Schema.COL_ID + "=" + id;
                if (Str.isNotNullOrEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                updatedRowsCounter = db.update(
                        UsersContract.Schema.TABLE_NAME,
                        values,
                        where,
                        whereArgs
                );
                break;
            case PROFILES:
                updatedRowsCounter = db.update(UserProfilesContract.Schema.TABLE_NAME, values, whereSelection, whereArgs);
                break;
            case PROFILE_ID:
                id = uri.getLastPathSegment();
                where = UserProfilesContract.Schema.COL_ID + "=" + id;
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
        SQLiteDatabase db = dbManager.getUserDBHelper().getWritableDatabase();

        int deletedRowsCounter;
        String id;
        String where;

        switch (URI_MATCHER.match(uri)) {

            case USERS:
                deletedRowsCounter = db.delete(UsersContract.Schema.TABLE_NAME, whereSelection, whereArgs);
                break;

            case USER_ID:
                id = uri.getLastPathSegment();
                where = UsersContract.Schema.COL_ID + "=" + id;
                if (Str.isNotNullOrEmpty(whereSelection)) {
                    where += " AND (" + whereSelection + ")";
                }
                deletedRowsCounter = db.delete(
                        UsersContract.Schema.TABLE_NAME,
                        where,
                        whereArgs
                );
                break;

            case PROFILES:
                deletedRowsCounter = db.delete(UserProfilesContract.Schema.TABLE_NAME, whereSelection, whereArgs);
                break;

            case PROFILE_ID:
                id = uri.getLastPathSegment();
                where = UserProfilesContract.Schema.COL_ID + "=" + id;
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
