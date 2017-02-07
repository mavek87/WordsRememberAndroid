package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManagementPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManipulationPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, ModelModule.class})
public interface ModelComponent {

    void inject(DictionaryManagementPresenterFactory managementPresenterFactory);

    void inject(DictionaryManipulationPresenterFactory manipulationPresenterFactory);

}
