package com.matteoveroni.wordsremember.dependency_injection;

import android.app.Application;
import android.content.Context;

import com.matteoveroni.wordsremember.dependency_injection.components.DaggerPresentersComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.PresentersComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.PresentersModule;

public class AppDependencies extends Application {

    private PresentersComponent presentersComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        presentersComponent = DaggerPresentersComponent.builder().presentersModule(new PresentersModule()).build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public PresentersComponent getPresentersComponent() {
        return presentersComponent;
    }

    public static AppDependencies getInjectorForApp(Context context){
        return (AppDependencies) context.getApplicationContext();
    }

//    public static PresentersComponent getPresentersComponent(Context context) {
//        return ((AppDependencies) context.getApplicationContext()).presentersComponent;
//    }
}
