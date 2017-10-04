package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DictionaryModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.SettingsModule;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.quizgame.business_logic.presenter.QuizGamePresenterFactory;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenterFactory;
import com.matteoveroni.wordsremember.login.LoginPresenterFactory;


import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {AppModule.class, DaoModule.class, DictionaryModelModule.class, SettingsModule.class})
public interface AppComponent {

    void inject(LoginPresenterFactory loginPresenterFactory);

    void inject(ManageVocablesPresenterFactory vocableManagerPresenterFactory);

    void inject(EditVocablePresenterFactory vocableEditorPresenterFactory);

    void inject(EditTranslationPresenterFactory translationEditorPresenterFactory);

    void inject(AddTranslationPresenterFactory addTranslationPresenterFactory);

    void inject(QuizGamePresenterFactory quizGamePresenterFactory);

    void inject(SettingsPresenterFactory settingsPresenterFactory);
}
