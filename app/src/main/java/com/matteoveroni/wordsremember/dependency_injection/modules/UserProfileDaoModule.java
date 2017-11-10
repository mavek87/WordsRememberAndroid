package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class UserProfileDaoModule {

    public UserProfileDaoModule() {
    }

    @Provides
    @Singleton
    public UserProfilesDAO providesUserProfileDAO(Context context) {
        return new UserProfilesDAO(context);
    }
}
