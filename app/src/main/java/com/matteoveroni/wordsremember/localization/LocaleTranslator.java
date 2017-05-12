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
            throw new RuntimeException("Translation for " + locale_keyword + " not found");
        }
    }
}
