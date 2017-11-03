package com.matteoveroni.wordsremember.settings.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.persistency.DatabaseManager;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.settings.view.SettingsView;

import org.joda.time.DateTime;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenter implements Presenter<SettingsView> {

    public static final String TAG = TagGenerator.tag(SettingsPresenter.class);

    private final Settings settings;
    private SettingsView view;

    public SettingsPresenter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void attachView(SettingsView view) {
        this.view = view;
        Log.d(TAG, "View Attached");
        showLastGameDate();
        showGameDifficultyInView();
        if (settings.isOnlineTranslationServiceEnabled()) {
            view.checkOnlineTranslationsCheckPreference(true);
        } else {
            view.checkOnlineTranslationsCheckPreference(false);
        }
    }

    @Override
    public void detachView() {
        this.view = null;
        Log.d(TAG, "View Destroyed");
    }

    private void showLastGameDate() {
        DateTime lastGameDate = settings.getLastGameDate();
        if (lastGameDate != null) {
            String str_date = lastGameDate.toLocalDate().toString();
            String str_time = lastGameDate.getHourOfDay() + ":" + lastGameDate.getMinuteOfHour() + ":" + lastGameDate.getSecondOfMinute();
            view.setLastGameDate(new FormattedString("%s: %s - %s", LocaleKey.LAST_GAME_DATE, str_date, str_time));
        } else {
            view.setLastGameDate(new FormattedString("%s: - ", LocaleKey.LAST_GAME_DATE));
        }
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

    public void onOnlineTranslationsCheckSelected(boolean value) {
        settings.setOnlineTranslationServiceEnabled(value);
    }
}
