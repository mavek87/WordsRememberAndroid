package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.matteoveroni.myutils.MyUtilsVersion;
import com.matteoveroni.wordsremember.dependency_injection.components.AppComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.DaggerAppComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DictionaryDaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DatabaseManagerModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DictionaryModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.SettingsModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.UserProfileDaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.UserProfileModelModule;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import java.util.Locale;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.5.7
 **/

public class WordsRemember extends Application {

    public static final String APP_NAME = WordsRemember.class.getSimpleName();
    public static final String LOWERCASE_APP_NAME = APP_NAME.toLowerCase();
    public static final String ABBREVIATED_NAME = "WR";
    public static final String VERSION = "0.5.7";
    public static final String AUTHOR = "Matteo Veroni";
    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();
    public static Locale CURRENT_LOCALE;

    //    private static final boolean CLEAR_DB_BEFORE_EACH_LOGIN = false;
//    private static final boolean POPULATE_DB_USING_FAKE_DATA = false;
    private static final boolean CLEAR_PREFERENCES_EACH_TIME = false;

    private static AppComponent APP_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();

        CURRENT_LOCALE = LocaleTranslator.getLocale(getApplicationContext());
        printAppSpecs();
        buildAppModules();

        if (CLEAR_PREFERENCES_EACH_TIME)
            getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit().clear().apply();

//        if (CLEAR_DB_BEFORE_EACH_LOGIN)
//            DatabaseHelper.getInstance(getApplicationContext()).deleteCurrentUserDB();
//
//        if (POPULATE_DB_USING_FAKE_DATA) {
//            // (mode "true" broke some test)
//            int NUMBER_OF_FAKE_VOCABLES_TO_CREATE = 1;
//            populateDatabaseUsingFakeData(getApplicationContext(), NUMBER_OF_FAKE_VOCABLES_TO_CREATE);
//        }
    }

    private void buildAppModules() {
        APP_COMPONENT = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .databaseManagerModule(new DatabaseManagerModule())
                .settingsModule(new SettingsModule())
                .userProfileModelModule(new UserProfileModelModule())
                .userProfileDaoModule(new UserProfileDaoModule())
                .dictionaryModelModule(new DictionaryModelModule())
                .dictionaryDaoModule(new DictionaryDaoModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return APP_COMPONENT;
    }

    @Deprecated
    public static LocaleTranslator getLocaleTranslator(Context context) {
        return new LocaleTranslator(context);
    }

    public static WordsRemember getInjectorsForApp(Context context) {
        return (WordsRemember) context.getApplicationContext();
    }

    private void printAppSpecs() {
        Log.i(APP_NAME, "APP_NAME: ".concat(APP_NAME));
        Log.d(APP_NAME, "LOWERCASE_APP_NAME: ".concat(LOWERCASE_APP_NAME));
        Log.i(APP_NAME, "VERSION: ".concat(VERSION));
        Log.d(APP_NAME, "AUTHOR: ".concat(AUTHOR));
        Log.d(APP_NAME, "AUTHORITY: ".concat(AUTHORITY));
        Log.d(APP_NAME, "MYUTILS VERSION: ".concat(MyUtilsVersion.NUMBER));
        Log.i(APP_NAME, "CURRENT LOCALE: ".concat(CURRENT_LOCALE.toString()));
    }

//    private void populateDatabaseUsingFakeData(Context context, int numberOfVocablesToCreate) {
//        for (int i = 0; i < numberOfVocablesToCreate; i++) {
//            Word vocableToSave = new Word(Str.generateUniqueRndLowercaseString(new IntRange(3, 20)));
//            DictionaryDAO dao = new DictionaryDAO(context);
//            dao.saveVocable(vocableToSave);
//        }
//    }
}
