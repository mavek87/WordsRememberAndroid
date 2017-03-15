package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dependency_injection.components.DAOComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.DaggerDAOComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DaoModule;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.0.62
 **/

public class WordsRemember extends Application {

    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();
    public static final String NAME = TagGenerator.tag(WordsRemember.class);
    public static final String LOWERCASE_NAME = NAME.toLowerCase();
    public static final String ABBREVIATED_NAME = "WR";
    public static final String VERSION = "0.0.62";

    private static final DictionaryModel DICTIONARY_MODEL = new DictionaryModel();
    private static DAOComponent DAO_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();
        printAppSpecs();
        buildDAOComponent();
    }

    public static DictionaryModel getDictionaryModel() {
        return DICTIONARY_MODEL;
    }

    public static DAOComponent getDAOComponent() {
        return DAO_COMPONENT;
    }

    public static WordsRemember getInjectorsForApp(Context context) {
        return (WordsRemember) context.getApplicationContext();
    }

    private void printAppSpecs() {
        Log.i(NAME, Str.concat("AUTHORITY = ", AUTHORITY));
        Log.i(NAME, Str.concat("NAME = ", NAME));
        Log.i(NAME, Str.concat("LOWERCASE_NAME = ", LOWERCASE_NAME));
        Log.i(NAME, Str.concat("VERSION = ", VERSION));
    }

    private void buildDAOComponent() {
        // buildDAOComponent Dagger2 component
        DAO_COMPONENT = DaggerDAOComponent
                .builder()
                .appModule(new AppModule(this))
                .daoModule(new DaoModule())
                .build();
    }

}
