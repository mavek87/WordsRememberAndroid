package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class ProfilesDBManagerModule {

    public ProfilesDBManagerModule() {
    }

    @Provides
    @Singleton
    public ProfilesDBManager providesProfilesDBManager(Context context) {
        return ProfilesDBManager.getInstance(context);
    }
}
