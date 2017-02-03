package com.matteoveroni.wordsremember;

import android.app.Application;
import android.content.Context;

import com.matteoveroni.wordsremember.dependency_injection.components.DaggerModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.ModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;

/**
 * Class which extends Application
 * Dagger2 components for dependency injection are built here
 *
 * @author Matteo Veroni
 * @version 0.0.22
 */
public class MyApp extends Application {

    public static final String NAME = "wordsremember";
    public static final String NAME_TO_DISPLAY = "WordsRemember";
    public static final String VERSION = "0.0.22";
    public static final String AUTHORITY = MyApp.class.getPackage().getName();

    private static ModelComponent MODEL_COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger2 component. Used for injecting models to each class specified by ModelComponent interface.
        MODEL_COMPONENT = DaggerModelComponent
                .builder()
                .appModule(new AppModule(this))
                .modelModule(new ModelModule())
                .build();
    }

    public static ModelComponent getModelComponent() {
        return MODEL_COMPONENT;
    }

    public static MyApp getInjectorsForApp(Context context) {
        return (MyApp) context.getApplicationContext();
    }
}
