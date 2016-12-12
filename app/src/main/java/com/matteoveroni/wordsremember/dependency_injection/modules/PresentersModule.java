package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.app.Application;

import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {
    @Provides
    DictionaryManagementPresenter provideDictionaryActivityPresenter() {
        return new DictionaryManagementActivityPresenter();
    }
}
