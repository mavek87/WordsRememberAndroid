package com.matteoveroni.wordsremember.persistency.providers;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.UsersContract;

/**
 * @author Matteo Veroni
 */

public class UsersProvider extends ExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(UsersProvider.class);
    public static final String CONTENT_AUTHORITY = UsersProvider.class.getPackage().getName();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int USERNAME = 1;
    private static final int EMAIL = 2;

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UsersContract.NAME, USERNAME);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, UsersContract.NAME + "/#", EMAIL);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long id = db.insertOrThrow(UsersContract.Schema.TABLE_NAME, null, values);

        notifyChangeToObservingCursors(uri);
        return Uri.parse(uri + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int deletedRowsCount = db.delete(UsersContract.Schema.TABLE_NAME, whereClause, whereArgs);

        notifyChangeToObservingCursors(uri);
        return deletedRowsCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
