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
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.0.67
 **/

public class WordsRemember extends Application {

    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();
    public static final String NAME = TagGenerator.tag(WordsRemember.class);
    public static final String LOWERCASE_NAME = NAME.toLowerCase();
    public static final String ABBREVIATED_NAME = "WR";
    public static final String VERSION = "0.0.67";

    private static final boolean PRODUCTION_MODE = true;

    private static final DictionaryModel DICTIONARY_MODEL = new DictionaryModel();
    private static DAOComponent DAO_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();
        printAppSpecs();
        buildDAOComponent();
        if (!PRODUCTION_MODE) {
            populateDatabaseForTestPurposes(getApplicationContext());
        }
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
        // build Dagger2 DAOcomponent
        DAO_COMPONENT = DaggerDAOComponent
                .builder()
                .appModule(new AppModule(this))
                .daoModule(new DaoModule())
                .build();
    }

    //TODO: remove this method in production code
    private void populateDatabaseForTestPurposes(Context context) {
        final int NUMBER_OF_VOCABLES_TO_CREATE = 1;
        for (int i = 0; i < NUMBER_OF_VOCABLES_TO_CREATE; i++) {
            Word vocableToSave = new Word(Str.generateUniqueRndLowercaseString(new Range(3, 20)));
            DictionaryDAO dao = new DictionaryDAO(context);
            dao.saveVocable(vocableToSave);
        }
    }

}
