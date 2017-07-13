package com.matteoveroni.wordsremember.persistency.providers;

import android.content.ContentProvider;
import android.net.Uri;
import android.text.TextUtils;

import com.matteoveroni.wordsremember.persistency.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which extends ContentProvider to allow the Content Resolver
 * to perform queries using the limit parameter.
 *
 * @author Matteo Veroni
 */

public abstract class ExtendedQueriesContentProvider extends ContentProvider {

    public static final String SCHEME = "content://";
    public static final String QUERY_PARAMETER_LIMIT = "LIMIT";
    public static final String QUERY_PARAMETER_OFFSET = "OFFSET";

    protected DatabaseManager databaseManager;

    @Override
    public boolean onCreate() {
        databaseManager = DatabaseManager.getInstance(getContext());
        return true;
    }

    /**
     * @return parameter LIMIT value of a SQL Query
     */
    public static String getQueryParameterLimitValue(Uri uri) {
        return uri.getQueryParameter(QUERY_PARAMETER_LIMIT);
    }

    /**
     * @return parameter OFFSET value of a SQL Query
     */
    public static String getQueryParameterOffsetValue(Uri uri) {
        return uri.getQueryParameter(QUERY_PARAMETER_OFFSET);
    }

    public static String getQueryParametersValuesForLimitAndOffset(Uri uri) {
        final List<String> limitAndOffset = new ArrayList<>();

        if (isQueryParameterLimitSet(uri))
            limitAndOffset.add(getQueryParameterLimitValue(uri));

        if (isQueryParameterOffsetSet(uri))
            limitAndOffset.add(getQueryParameterOffsetValue(uri));

        return TextUtils.join(" ", limitAndOffset);
    }

    public static boolean isQueryParameterLimitSet(Uri uri) {
        return (getQueryParameterLimitValue(uri) != null);
    }

    public static boolean isQueryParameterOffsetSet(Uri uri) {
        return (getQueryParameterOffsetValue(uri) != null);
    }
}
