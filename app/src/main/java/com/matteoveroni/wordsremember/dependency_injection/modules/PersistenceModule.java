package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.persistency.dao.StatisticsDAO;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class PersistenceModule {

    public PersistenceModule() {
    }

    @Provides
    @Singleton
    public DBManager provideProfilesDBManager(Context context) {
        return DBManager.getInstance(context);
    }

    @Provides
    @Singleton
    public DictionaryDAO provideDictionaryDAO(Context context) {
        return new DictionaryDAO(context);
    }

    @Provides
    @Singleton
    public UserProfilesDAO provideUserProfileDAO(Context context, DBManager dbManager) {
        return new UserProfilesDAO(context, dbManager);
    }

    @Provides
    @Singleton
    public StatisticsDAO provideStatisticsDAO(Context context, DBManager dbManager) {
        return new StatisticsDAO(context, dbManager);
    }
}
