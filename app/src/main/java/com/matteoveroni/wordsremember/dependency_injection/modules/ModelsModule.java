package com.matteoveroni.wordsremember.dependency_injection.modules;

import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class ModelsModule {
    public ModelsModule() {
    }

    @Provides
    @Singleton
    DictionaryModel provideDictionaryModel() {
        return new DictionaryModel();
    }

    @Provides
    @Singleton
    UserProfileModel provideUserProfileModel() {
        return new UserProfileModel();
    }
}
