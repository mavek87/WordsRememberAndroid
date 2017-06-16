package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.myutils.MyUtilsVersion;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dependency_injection.components.AppComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.DaggerAppComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.SettingsModule;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.provider.DatabaseManager;

import java.util.Locale;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.1.60
 **/

public class WordsRemember extends Application {

    public static final String APP_NAME = WordsRemember.class.getSimpleName();
    public static final String LOWERCASE_APP_NAME = APP_NAME.toLowerCase();
    public static final String ABBREVIATED_NAME = "WR";
    public static final String VERSION = "0.1.60";
    public static final String AUTHOR = "Matteo Veroni";
    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();
    public static final DictionaryModel DICTIONARY_MODEL = new DictionaryModel();
    public static Locale CURRENT_LOCALE;

    private static final boolean START_WITH_EMPTY_DB = false;
    private static final boolean POPULATE_DB_USING_FAKE_DATA = false;

    private static AppComponent APP_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();

        CURRENT_LOCALE = LocaleTranslator.getLocale(getApplicationContext());
        printAppSpecs();
        buildAppComponent();

        if (START_WITH_EMPTY_DB)
            DatabaseManager.getInstance(getApplicationContext()).deleteDatabase();

        if (POPULATE_DB_USING_FAKE_DATA) {
            // (mode "true" broke some test)
            int NUMBER_OF_VOCABLES_TO_CREATE = 1;
            populateDatabaseUsingFakeData(getApplicationContext(), NUMBER_OF_VOCABLES_TO_CREATE);
        }
    }

    private void buildAppComponent() {
        APP_COMPONENT = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .daoModule(new DaoModule())
                .settingsModule(new SettingsModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return APP_COMPONENT;
    }

    public static LocaleTranslator getLocaleTranslator(Context context) {
        return new LocaleTranslator(context);
    }

    public static WordsRemember getInjectorsForApp(Context context) {
        return (WordsRemember) context.getApplicationContext();
    }

    private void printAppSpecs() {
        Log.i(APP_NAME, Str.concat("APP_NAME: ", APP_NAME));
        Log.d(APP_NAME, Str.concat("LOWERCASE_APP_NAME: ", LOWERCASE_APP_NAME));
        Log.i(APP_NAME, Str.concat("VERSION: ", VERSION));
        Log.d(APP_NAME, Str.concat("AUTHOR: ", AUTHOR));
        Log.d(APP_NAME, Str.concat("AUTHORITY: ", AUTHORITY));
        Log.d(APP_NAME, Str.concat("MYUTILS VERSION: ", MyUtilsVersion.NUMBER));
        Log.i(APP_NAME, Str.concat("CURRENT LOCALE: ", CURRENT_LOCALE.toString()));
    }

    private void populateDatabaseUsingFakeData(Context context, int numberOfVocablesToCreate) {
        for (int i = 0; i < numberOfVocablesToCreate; i++) {
            Word vocableToSave = new Word(Str.generateUniqueRndLowercaseString(new IntRange(3, 20)));
            DictionaryDAO dao = new DictionaryDAO(context);
            dao.saveVocable(vocableToSave);
        }
    }
}
