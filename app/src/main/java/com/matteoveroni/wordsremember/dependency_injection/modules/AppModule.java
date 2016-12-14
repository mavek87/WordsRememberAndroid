package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.dependency_injection.MyApp;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private MyApp app;

    public AppModule(MyApp app) {
        this.app = app;
    }

    @Provides
    Context provideApplicationContext() {
        return app.getApplicationContext();
    }
}