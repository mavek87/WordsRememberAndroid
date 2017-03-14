package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.ModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.TranslationSelectorPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.VocableEditPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.VocablesManagerPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.TranslationEditPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, ModelModule.class})
public interface ModelComponent {

    void inject(VocablesManagerPresenterFactory vocableManagerPresenterFactory);

    void inject(VocableEditPresenterFactory vocableEditorPresenterFactory);

    void inject(TranslationEditPresenterFactory translationEditorPresenterFactory);

    void inject(TranslationSelectorPresenterFactory translationSelectorPresenterFactory);

}
