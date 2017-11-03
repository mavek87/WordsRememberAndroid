package com.matteoveroni.wordsremember.dependency_injection.modules;

import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class DictionaryModelModule {

    public DictionaryModelModule() {
    }

    @Provides
    @Singleton
    DictionaryModel provideDictionaryModel() {
        return new DictionaryModel();
    }
}
