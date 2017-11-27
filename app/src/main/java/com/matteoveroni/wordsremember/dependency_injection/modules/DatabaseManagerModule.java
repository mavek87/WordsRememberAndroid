package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.DBUserManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class DatabaseManagerModule {

    public DatabaseManagerModule() {
    }

    @Provides
    @Singleton
    public DBUserManager providesDatabaseManager(Context context) {
        return DBUserManager.getInstance(context);
    }
}
