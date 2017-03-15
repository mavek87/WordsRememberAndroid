package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class DaoModule {

    public DaoModule() {
    }

    @Provides
    public DictionaryDAO provideDictionaryDAO(Context context) {
        return new DictionaryDAO(context);
    }
}
