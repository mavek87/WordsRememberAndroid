package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.matteoveroni.wordsremember.dependency_injection.components.DaggerModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.ModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.utilities.Str;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

/**
 * Class which extends Application. Dagger2 components for dependency injection are built here.
 *
 * @author Matteo Veroni
 * @version 0.0.28
 */
public class WordsRemember extends Application {

    public static final String AUTHORITY = WordsRemember.class.getPackage().getName();
    public static final String TAG = TagGenerator.tag(WordsRemember.class);
    public static final String NAME = TAG.toLowerCase();
    public static final String VERSION = "0.0.27";

    private static ModelComponent MODEL_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();
        printAppSpecs();
        injectModels();
    }

    public static ModelComponent getModelComponent() {
        return MODEL_COMPONENT;
    }

    public static WordsRemember getInjectorsForApp(Context context) {
        return (WordsRemember) context.getApplicationContext();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void printAppSpecs() {
        Log.i(TAG, Str.concat("AUTHORITY = ", AUTHORITY));
        Log.i(TAG, Str.concat("TAG = ", TAG));
        Log.i(TAG, Str.concat("NAME = ", NAME));
        Log.i(TAG, Str.concat("VERSION = ", VERSION));
    }

    private void injectModels() {
        // Dagger2 component. Used for injecting models to each class specified by ModelComponent interface.
        MODEL_COMPONENT = DaggerModelComponent
                .builder()
                .appModule(new AppModule(this))
                .modelModule(new ModelModule())
                .build();
    }

}
