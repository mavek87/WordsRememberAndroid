package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.DaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.ManageVocablesPresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, DaoModule.class})
public interface DAOComponent {

    void inject(ManageVocablesPresenterFactory vocableManagerPresenterFactory);

    void inject(EditVocablePresenterFactory vocableEditorPresenterFactory);

    void inject(EditTranslationPresenterFactory translationEditorPresenterFactory);

    void inject(AddTranslationPresenterFactory addTranslationPresenterFactory);

}
