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
 * @version 0.0.11
 */
public class MyApp extends Application {

    private static ModelComponent modelComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger2 component. Used for injecting models to each class specified by ModelComponent interface.
        modelComponent = DaggerModelComponent
                .builder()
                .appModule(new AppModule(this))
                .modelModule(new ModelModule())
                .build();
    }

    public static ModelComponent getModelComponent() {
        return modelComponent;
    }

    public static MyApp getInjectorsForApp(Context context) {
        return (MyApp) context.getApplicationContext();
    }
}
