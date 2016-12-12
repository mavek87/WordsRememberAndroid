package com.matteoveroni.wordsremember.dependency_injection;

import android.app.Application;
import android.content.Context;

//import com.matteoveroni.wordsremember.dependency_injection.components.DaggerPresentersComponent;
//import com.matteoveroni.wordsremember.dependency_injection.components.DaggerModelsComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.ModelsComponent;
import com.matteoveroni.wordsremember.dependency_injection.components.PresentersComponent;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.ModelsModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.PresentersModule;

public class AppDependencies extends Application {

    //    private PresentersComponent presentersComponent;
    private ModelsComponent modelsComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
//        presentersComponent = DaggerPresentersComponent.builder().presentersModule(new PresentersModule()).build();

//        modelsComponent = DaggerModelsComponent
//                .builder()
//                .appModule(new AppModule(this))
//                .modelsModule(new ModelsModule())
//                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

//    public PresentersComponent getPresentersComponent() {
//        return presentersComponent;
//    }

    public ModelsComponent getModelsComponent() {
        return modelsComponent;
    }

    public static AppDependencies getInjectorForApp(Context context) {
        return (AppDependencies) context.getApplicationContext();
    }

//    public static PresentersComponent getPresentersComponent(Context context) {
//        return ((AppDependencies) context.getApplicationContext()).presentersComponent;
//    }
}
