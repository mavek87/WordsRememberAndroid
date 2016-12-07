package com.matteoveroni.wordsremember.models.provider;

import android.content.ContentProvider;
import android.net.Uri;

/**
 * Abstract class which extends ContentProvider to allow the Content Resolver
 * to perform queries using the limit parameter.
 *
 * @author Matteo Veroni
 */
public abstract class ExtendedQueriesContentProvider extends ContentProvider {
    public static final String QUERY_PARAMETER_LIMIT = "limit";

    /**
     * @return parameter limit value of a SQL Query
     */
    public static final String getQueryParameterLimitValue(Uri uri) {
        return uri.getQueryParameter(QUERY_PARAMETER_LIMIT);
    }

}
