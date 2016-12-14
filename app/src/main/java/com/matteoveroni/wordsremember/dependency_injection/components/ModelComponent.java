package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ModelModule.class})
public interface ModelComponent {
    void inject(DictionaryManagementPresenterFactory presenterFactory);
}
