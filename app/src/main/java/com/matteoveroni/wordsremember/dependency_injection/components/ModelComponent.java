package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.DictionaryVocableEditorPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.DictionaryVocablesManagerPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.DictionaryTranslationEditorPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, ModelModule.class})
public interface ModelComponent {

    void inject(DictionaryVocablesManagerPresenterFactory vocableManagerPresenterFactory);

    void inject(DictionaryVocableEditorPresenterFactory vocableEditorPresenterFactory);

    void inject(DictionaryTranslationEditorPresenterFactory translationEditorPresenterFactory);
}
