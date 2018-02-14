package com.matteoveroni.wordsremember.factories;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.scene_login.LoginPresenterFactory;
import com.matteoveroni.wordsremember.scene_mainmenu.MainMenuPresenterFactory;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenterFactory;
import com.matteoveroni.wordsremember.scene_report.QuizGameReportPresenterFactory;
import com.matteoveroni.wordsremember.scene_settings.presenter.SettingsPresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.editor.presenter.UserProfileEditorPresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenterFactory;

/**
 * @author Matteo Veroni
 */

public class PresenterFactories {

    public static PresenterFactory getFactory(PresenterFactoryName factoryName) {
        switch (factoryName) {
            case MAIN_MENU_PRESENTER_FACTORY:
                return new MainMenuPresenterFactory();
            case LOGIN_PRESENTER_FACTORY:
                return new LoginPresenterFactory();
            case SETTINGS_PRESENTER_FACTORY:
                return new SettingsPresenterFactory();
            case USER_PROFILE_PRESENTER:
                return new UserProfilePresenterFactory();
            case USER_PROFILE_EDITOR_PRESENTER:
                return new UserProfileEditorPresenterFactory();
            case ADD_TRANSLATION_PRESENTER_FACTORY:
                return new AddTranslationPresenterFactory();
            case EDIT_TRANSLATION_PRESENTER_FACTORY:
                return new EditTranslationPresenterFactory();
            case EDIT_VOCABLE_PRESENTER_FACTORY:
                return new EditVocablePresenterFactory();
            case MANAGE_VOCABLE_PRESENTER_FACTORY:
                return new ManageVocablesPresenterFactory();
            case QUIZ_GAME_PRESENTER_FACTORY:
                return new QuizGamePresenterFactory();
            case QUIZ_GAME_REPORT_PRESENTER_FACTORY:
                return new QuizGameReportPresenterFactory();
            default:
                throw new RuntimeException("Unknown PresenterFactoryName");
        }
    }
}
