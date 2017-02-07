package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class ModelModule {

    public ModelModule() {
    }

    @Provides
    @SuppressWarnings("unused")
    public DictionaryDAO provideDictionaryModel(Context context) {
        return new DictionaryDAO(context);
    }
}
