package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class DictionaryDaoModule {

    public DictionaryDaoModule() {
    }

    @Provides
    @Singleton
    public DictionaryDAO providesDictionaryDAO(Context context) {
        return new DictionaryDAO(context);
    }
}
