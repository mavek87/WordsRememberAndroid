package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.MyApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private MyApp app;

    public AppModule(MyApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return app.getApplicationContext();
    }
}