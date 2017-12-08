package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class UserProfileModelModule {

    public UserProfileModelModule() {
    }

    @Provides
    @Singleton
    public UserProfileModel providesUserProfileModel() {
        return new UserProfileModel();
    }
}
