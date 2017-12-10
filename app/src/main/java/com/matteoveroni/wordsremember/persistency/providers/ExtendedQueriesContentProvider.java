package com.matteoveroni.wordsremember.persistency.providers;

import android.content.ContentProvider;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    protected ProfilesDBManager profileDBManager;

    public class Error {
        public static final String UNSUPPORTED_URI = "Unsupported URI";
    }

    @Override
    public boolean onCreate() {
        profileDBManager = ProfilesDBManager.getInstance(getContext());
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

    // https://stackoverflow.com/questions/40481035/diference-between-cursor-setnotificationuri-and-getcontentresolver-notifycha

    protected void notifyChangeToObservingCursors(Uri uri) {
        if (isContentResolverNotNull()) getContext().getContentResolver().notifyChange(uri, null);
    }

    protected boolean isContentResolverNotNull() {
        Context ctx = getContext();
        return (ctx != null && ctx.getContentResolver() != null);
    }
}
