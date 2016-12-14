package com.matteoveroni.wordsremember.dependency_injection;

import android.app.Application;
import android.content.Context;

import com.matteoveroni.wordsremember.dependency_injection.components.DaggerModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.ModelComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;

public class MyApp extends Application {

    private static ModelComponent modelComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
//        presentersComponent = DaggerPresentersComponent.builder().presentersModule(new PresentersModule()).build();

        modelComponent = DaggerModelComponent
                .builder()
                .appModule(new AppModule(this))
                .modelModule(new ModelModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

//    public PresentersComponent getPresentersComponent() {
//        return presentersComponent;
//    }

    public static ModelComponent getModelComponent() {
        return modelComponent;
    }

    public static MyApp getInjectorsForApp(Context context) {
        return (MyApp) context.getApplicationContext();
    }

//    public static PresentersComponent getPresentersComponent(Context context) {
//        return ((MyApp) context.getApplicationContext()).presentersComponent;
//    }
}
