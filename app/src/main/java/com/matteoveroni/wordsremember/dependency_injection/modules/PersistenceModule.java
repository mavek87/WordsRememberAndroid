package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;
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
    public ProfilesDBManager provideProfilesDBManager(Context context) {
        return ProfilesDBManager.getInstance(context);
    }

    @Provides
    @Singleton
    public DictionaryDAO provideDictionaryDAO(Context context) {
        return new DictionaryDAO(context);
    }

    @Provides
    @Singleton
    public UserProfilesDAO provideUserProfileDAO(Context context, ProfilesDBManager profilesDBManager) {
        return new UserProfilesDAO(context, profilesDBManager);
    }

    @Provides
    @Singleton
    public StatisticsDAO provideStatisticsDAO(Context context, ProfilesDBManager profilesDBManager) {
        return new StatisticsDAO(context, profilesDBManager);
    }
}
