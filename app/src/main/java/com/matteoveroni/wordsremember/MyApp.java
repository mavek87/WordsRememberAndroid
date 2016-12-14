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
 * @version 0.0.9
 */
public class MyApp extends Application {

    private static ModelComponent modelComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Dagger2 component. Used for injecting models to each class specified by ModelComponent interface.
         */
        modelComponent = DaggerModelComponent
                .builder()
                .appModule(new AppModule(this))
                .modelModule(new ModelModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public static ModelComponent getModelComponent() {
        return modelComponent;
    }

    public static MyApp getInjectorsForApp(Context context) {
        return (MyApp) context.getApplicationContext();
    }
}
