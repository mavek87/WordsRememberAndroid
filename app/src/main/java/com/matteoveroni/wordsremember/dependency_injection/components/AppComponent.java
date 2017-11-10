package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DatabaseManagerModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DictionaryDaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.DictionaryModelModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.SettingsModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.UserProfileDaoModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.UserProfileModelModule;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.scene_login.LoginPresenterFactory;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenterFactory;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_settings.presenter.SettingsPresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.editor.presenter.UserProfileEditorPresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matteo Veroni
 */

@Singleton
@Component(modules = {
        AppModule.class,
        DatabaseManagerModule.class,
        SettingsModule.class,
        UserProfileModelModule.class,
        UserProfileDaoModule.class,
        DictionaryDaoModule.class,
        DictionaryModelModule.class,
})
public interface AppComponent {

    void inject(LoginPresenterFactory loginPresenterFactory);

    void inject(ManageVocablesPresenterFactory vocableManagerPresenterFactory);

    void inject(EditVocablePresenterFactory vocableEditorPresenterFactory);

    void inject(EditTranslationPresenterFactory translationEditorPresenterFactory);

    void inject(AddTranslationPresenterFactory addTranslationPresenterFactory);

    void inject(QuizGamePresenterFactory quizGamePresenterFactory);

    void inject(SettingsPresenterFactory settingsPresenterFactory);

    void inject(UserProfilePresenterFactory userProfilePresenterFactory);

    void inject(UserProfileEditorPresenterFactory userProfilePresenterFactory);

    void inject(Settings settings);
}
