package com.matteoveroni.wordsremember.interfaces.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.AddTranslationActivity;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.EditTranslationActivity;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.EditVocableActivity;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.ManageVocablesActivity;
import com.matteoveroni.wordsremember.scene_login.LoginActivity;
import com.matteoveroni.wordsremember.scene_mainmenu.MainMenuActivity;
import com.matteoveroni.wordsremember.scene_userprofile.editor.UserProfileEditorActivity;
import com.matteoveroni.wordsremember.scene_userprofile.management.UserProfileActivity;
import com.matteoveroni.wordsremember.scene_quizgame.view.QuizGameActivity;
import com.matteoveroni.wordsremember.scene_settings.view.SettingsActivity;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String message);

    void showMessage(FormattedString formattedString);

    void switchToView(View.Name viewName);

    void switchToView(View.Name viewName, int requestCode);

    enum Name {
        LOGIN(LoginActivity.class),
        USER_PROFILES(UserProfileActivity.class),
        USER_PROFILE_EDITOR(UserProfileEditorActivity.class),
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
