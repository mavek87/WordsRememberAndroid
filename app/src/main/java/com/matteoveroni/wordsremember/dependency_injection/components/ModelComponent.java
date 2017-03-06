package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryVocableEditorPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryVocablesManagerPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationEditorPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, ModelModule.class})
public interface ModelComponent {

    void inject(DictionaryVocablesManagerPresenterFactory managementPresenterFactory);

    void inject(DictionaryVocableEditorPresenterFactory manipulationPresenterFactory);

    void inject(DictionaryTranslationEditorPresenterFactory translationsManipulationPresenterFactory);
}
