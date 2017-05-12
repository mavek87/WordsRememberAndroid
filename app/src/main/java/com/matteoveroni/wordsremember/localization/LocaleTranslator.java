package com.matteoveroni.wordsremember.localization;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;

/**
 * @author Matteo Veroni
 */

public class LocaleTranslator {

    public static final String TAG = TagGenerator.tag(LocaleTranslator.class);

    private final Context context;
    private final Resources androidResources;

    public LocaleTranslator(Context context) {
        this.context = context;
        this.androidResources = context.getResources();
        Log.d(TAG, "LocaleTranslator created");
    }

    public String localize(String locale_keyword) {
        try {
            int translation_id = androidResources.getIdentifier(locale_keyword, "string", context.getPackageName());
            return androidResources.getString(translation_id);
        } catch (Exception ex) {
            Log.e(TAG, "Translation for " + locale_keyword + " locale keyword not found");
            return locale_keyword;
        }
    }

    public String localize(FormattedLocaleString formattedLocaleString) {
        Object[] placeholders = formattedLocaleString.getArgs();
        int numberOfPlaceholders = (placeholders == null) ? 0 : placeholders.length;

        for (int i = 0; i < numberOfPlaceholders; i++) {
            if (placeholders[i] instanceof String) {
                placeholders[i] = localize((String) placeholders[i]);
            }
        }

        return String.format(formattedLocaleString.getString(), placeholders);
    }
}
