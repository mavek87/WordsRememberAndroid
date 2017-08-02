package com.matteoveroni.wordsremember.interfaces.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.dictionary.view.activities.AddTranslationActivity;
import com.matteoveroni.wordsremember.dictionary.view.activities.EditTranslationActivity;
import com.matteoveroni.wordsremember.dictionary.view.activities.EditVocableActivity;
import com.matteoveroni.wordsremember.dictionary.view.activities.ManageVocablesActivity;
import com.matteoveroni.wordsremember.login.LoginActivity;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameActivity;
import com.matteoveroni.wordsremember.settings.view.SettingsActivity;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String message);

    void showMessage(FormattedString formattedString);

    void switchView(View.Name viewName);

    public enum Name {
        LOGIN(LoginActivity.class),
        MAIN_MENU(MainMenuActivity.class),
        QUIZ_GAME(QuizGameActivity.class),
        SETTINGS(SettingsActivity.class),
        ADD_TRANSLATION(AddTranslationActivity.class),
        EDIT_TRANSLATION(EditTranslationActivity.class),
        EDIT_VOCABLE(EditVocableActivity.class),
        MANAGE_VOCABLES(ManageVocablesActivity.class);

        private Class classOfTheView;

        Name(Class classOfTheView) {
            this.classOfTheView = classOfTheView;
        }

        Class getViewClass() {
            return classOfTheView;
        }
    }
}
