package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Range;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dependency_injection.components.DAOComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.DaggerDAOComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DaoModule;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.0.98
 **/

public class WordsRemember extends Application {

    public static final String APP_NAME = TagGenerator.tag(WordsRemember.class);
    public static final String LOWERCASE_APP_NAME = APP_NAME.toLowerCase();
    public static final String ABBREVIATED_NAME = "WR";
    public static final String VERSION = "0.0.98";
    public static final String AUTHOR = "Matteo Veroni";
    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();

    private static final boolean START_WITH_EMPTY_DB = false;
    private static final boolean POPULATE_DB_USING_FAKE_DATA = false;

    public static final DictionaryModel DICTIONARY_MODEL = new DictionaryModel();

    public static LocaleTranslator LOCALE_TRANSLATOR;
    private static DAOComponent DAO_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();
        printAppSpecs();
        buildDAOComponent();
        initLocaleTranslator();
        if (START_WITH_EMPTY_DB) {
            DatabaseManager.getInstance(getApplicationContext()).deleteDatabase();
        }
        if (POPULATE_DB_USING_FAKE_DATA) {
            // (mode "true" broke some test)
            populateDatabaseForTestPurposes(getApplicationContext());
        }
    }

    public static DAOComponent getDAOComponent() {
        return DAO_COMPONENT;
    }

    public static WordsRemember getInjectorsForApp(Context context) {
        return (WordsRemember) context.getApplicationContext();
    }

    private void printAppSpecs() {
        Log.i(APP_NAME, Str.concat("APP_NAME: ", APP_NAME));
        Log.i(APP_NAME, Str.concat("LOWERCASE_APP_NAME: ", LOWERCASE_APP_NAME));
        Log.i(APP_NAME, Str.concat("VERSION: ", VERSION));
        Log.i(APP_NAME, Str.concat("AUTHOR: ", AUTHOR));
        Log.i(APP_NAME, Str.concat("AUTHORITY: ", AUTHORITY));
    }

    private void buildDAOComponent() {
        // build Dagger2 DAOcomponent
        DAO_COMPONENT = DaggerDAOComponent
                .builder()
                .appModule(new AppModule(this))
                .daoModule(new DaoModule())
                .build();
    }

    private void initLocaleTranslator() {
        LOCALE_TRANSLATOR = new LocaleTranslator(getApplicationContext());
    }

    private void populateDatabaseForTestPurposes(Context context) {
        final int NUMBER_OF_VOCABLES_TO_CREATE = 1;
        for (int i = 0; i < NUMBER_OF_VOCABLES_TO_CREATE; i++) {
            Word vocableToSave = new Word(Str.generateUniqueRndLowercaseString(new Range(3, 20)));
            DictionaryDAO dao = new DictionaryDAO(context);
            dao.saveVocable(vocableToSave);
        }
    }


}
