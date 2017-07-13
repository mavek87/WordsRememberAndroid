package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.matteoveroni.androidtaggenerator.TagGenerator;

/**
 * @author Matteo Veroni
 */

public class UsersProvider extends AbstractExtendedQueriesContentProvider {

    public static final String TAG = TagGenerator.tag(UsersProvider.class);
    public static final String CONTENT_AUTHORITY = UsersProvider.class.getPackage().getName();

    private DatabaseManager databaseManager;

    @Override
    public boolean onCreate() {
        databaseManager = DatabaseManager.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
