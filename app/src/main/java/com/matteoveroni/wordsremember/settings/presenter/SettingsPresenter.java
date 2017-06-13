package com.matteoveroni.wordsremember.settings.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.settings.view.SettingsView;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenter implements Presenter<SettingsView> {

    public static final String TAG = TagGenerator.tag(SettingsPresenter.class);

    private final Settings settings;
    private SettingsView view;

    public SettingsPresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
    }

    @Override
    public void attachView(SettingsView view) {
        this.view = view;
        Log.d(TAG, "View Attached");
        showGameDifficultyInView();
    }

    @Override
    public void detachView() {
        this.view = null;
        Log.d(TAG, "View Destroyed");
    }

    private void showGameDifficultyInView() {
        switch (settings.getDifficulty()) {
            case EASY:
                view.toggleEasyDifficulty();
                break;
            case MEDIUM:
                view.toggleMediumDifficulty();
                break;
            case HARD:
                view.toggleHardDifficulty();
                break;
        }
    }

    public void onGameDifficultySelected(QuizGameDifficulty difficulty) {
        settings.setDifficulty(difficulty);

        String difficultyTranslationKey = "";
        switch (difficulty) {
            case EASY:
                difficultyTranslationKey = LocaleKey.EASY;
                break;
            case MEDIUM:
                difficultyTranslationKey = LocaleKey.MEDIUM;
                break;
            case HARD:
                difficultyTranslationKey = LocaleKey.HARD;
                break;
        }

        FormattedString msg_gameDifficultyChanged = new FormattedString(
                "%s (%s)",
                LocaleKey.MSG_GAME_DIFFICULTY_CHANGED,
                difficultyTranslationKey
        );
        view.showMessage(msg_gameDifficultyChanged);
    }
}
