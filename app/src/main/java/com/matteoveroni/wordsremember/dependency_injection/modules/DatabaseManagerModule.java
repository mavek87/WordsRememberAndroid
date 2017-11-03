package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.DatabaseManager;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;

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
    public DatabaseManager providesDatabaseManager(Context context) {
        return DatabaseManager.getInstance(context);
    }
}
