package com.matteoveroni.wordsremember.dependency_injection.modules;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class DictionaryModelModule {

    private final DictionaryModel dictionaryModel = new DictionaryModel();

    @Provides
    @Singleton
    DictionaryModel provideDictionaryModel() {
        return dictionaryModel;
    }
}
