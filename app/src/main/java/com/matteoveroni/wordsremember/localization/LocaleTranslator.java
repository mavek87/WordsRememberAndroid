package com.matteoveroni.wordsremember.localization;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

    public String localize(String localeKey) {
        try {
            int translation_id = androidResources.getIdentifier(localeKey, "string", context.getPackageName());
            return androidResources.getString(translation_id);
        } catch (Exception ex) {
            Log.w(TAG, "Translation for " + localeKey + " locale keyword not found");
            return localeKey;
        }
    }

    public String localize(FormattedString formattedString) {
        Object[] placeholders = formattedString.getArgs();

        if (placeholders == null || placeholders.length == 0) {
            return localize(formattedString.getFormattedString());
        }

        for (int i = 0; i < placeholders.length; i++) {
            if (placeholders[i] instanceof String) {
                placeholders[i] = localize((String) placeholders[i]);
            }
        }

        return String.format(formattedString.getFormattedString(), placeholders);
    }

    public static Locale getLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static Locale[] getAvailableLocales() {
        return Locale.getAvailableLocales();
    }

    public static Map<String, Locale> getAvailableLocalesStringified() {
        SortedMap<String, Locale> map = new TreeMap<>();
        for (Locale locale : getAvailableLocales()) {
            map.put(stringifyLocale(locale), locale);
        }
        return map;
    }

    public static String stringifyLocale(Locale locale) {
        String localeToDisplay = locale.getDisplayLanguage(locale);
        String language = locale.getLanguage();
        String country = locale.getCountry();

        String languageAndCountry;
        if (language == null || language.trim().isEmpty()) {
            if (country == null || country.trim().isEmpty()) {
                languageAndCountry = "";
            } else {
                languageAndCountry = country;
            }
        } else {
            if (country == null || country.trim().isEmpty()) {
                languageAndCountry = language;
            } else {
                languageAndCountry = language + "_" + country;
            }
        }

        return "[" + languageAndCountry + "] - " + localeToDisplay + " - " + locale.getDisplayName();
    }
}
