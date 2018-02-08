package com.matteoveroni.wordsremember.scene_settings.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.localization.AndroidLocaleKey;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_settings.view.SettingsView;

import org.joda.time.DateTime;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenter extends BasePresenter<SettingsView> {

    public static final String TAG = TagGenerator.tag(SettingsPresenter.class);

    private final Settings settings;

    public SettingsPresenter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        showLastGameDate();
        showGameDifficultyInView();
        if (settings.isOnlineTranslationServiceEnabled()) {
            view.checkOnlineTranslationsCheckPreference(true);
        } else {
            view.checkOnlineTranslationsCheckPreference(false);
        }
    }

    private void showLastGameDate() {
        DateTime lastGameDate = settings.getLastGameDate();
        if (lastGameDate != null) {
            String str_date = lastGameDate.toLocalDate().toString();
            String str_time = lastGameDate.getHourOfDay() + ":" + lastGameDate.getMinuteOfHour() + ":" + lastGameDate.getSecondOfMinute();
            view.setLastGameDate(new FormattedString("%s: %s - %s", AndroidLocaleKey.LAST_GAME_DATE.getKeyName(), str_date, str_time));
        } else {
            view.setLastGameDate(new FormattedString("%s: - ", AndroidLocaleKey.LAST_GAME_DATE.getKeyName()));
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

    public void onGameDifficultySelected(GameDifficulty difficulty) {
        settings.setDifficulty(difficulty);

        String difficultyTranslationKey = "";
        switch (difficulty) {
            case EASY:
                difficultyTranslationKey = AndroidLocaleKey.EASY.getKeyName();
                break;
            case MEDIUM:
                difficultyTranslationKey = AndroidLocaleKey.MEDIUM.getKeyName();
                break;
            case HARD:
                difficultyTranslationKey = AndroidLocaleKey.HARD.getKeyName();
                break;
        }

        FormattedString msg_gameDifficultyChanged = new FormattedString(
                "%s (%s)",
                AndroidLocaleKey.MSG_GAME_DIFFICULTY_CHANGED.getKeyName(),
                difficultyTranslationKey
        );
        view.showMessage(msg_gameDifficultyChanged);
    }

    public void onOnlineTranslationsCheckSelected(boolean value) {
        settings.setOnlineTranslationServiceEnabled(value);
    }
}
