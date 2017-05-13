package com.matteoveroni.wordsremember.localization;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.FormattedString;

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

    public String localize(String localeStringKey) {
        try {
            int translation_id = androidResources.getIdentifier(localeStringKey, "string", context.getPackageName());
            return androidResources.getString(translation_id);
        } catch (Exception ex) {
            Log.w(TAG, "Translation for " + localeStringKey + " locale keyword not found");
            return localeStringKey;
        }
    }

    public String localize(FormattedString formattedString) {
        Object[] placeholders = formattedString.getArgs();

        if(placeholders == null || placeholders.length == 0) {
            return localize(formattedString.getFormattedString());
        }

        for (int i = 0; i < placeholders.length; i++) {
            if (placeholders[i] instanceof String) {
                placeholders[i] = localize((String) placeholders[i]);
            }
        }

        return String.format(formattedString.getFormattedString(), placeholders);
    }
}
