package com.matteoveroni.wordsremember.scene_settings.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.WordsRemember;
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
        populateView();
    }

    public void onGameDifficultyChanged(GameDifficulty difficulty) {
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

        view.showMessage(new FormattedString(
                "%s (%s)",
                AndroidLocaleKey.MSG_GAME_DIFFICULTY_CHANGED.getKeyName(),
                difficultyTranslationKey
        ));
    }

    public void onOnlineTranslationsCheckSelected(boolean value) {
        settings.setOnlineTranslationServiceEnabled(value);
    }

    private void populateView() {
        view.showLastGameDate(calculateLastGameDate());
        view.showDictionaryName(settings.getUserProfile().getName());
        try {
            view.showUsername(settings.getUser().getUsername());
        } catch (Settings.NoRegisteredUserException ex) {
            view.showUsername(" - ");
        }
        view.showGameVersion(WordsRemember.VERSION);
        view.showDeviceLocale();

        view.showGameDifficultyRadioButtonLabels();
        selectCurrentGameDifficultyInView();

        if (settings.isOnlineTranslationServiceEnabled()) {
            view.checkOnlineTranslationsCheckPreference(true);
        } else {
            view.checkOnlineTranslationsCheckPreference(false);
        }
    }

    private String calculateLastGameDate() {
        final DateTime lastGameDate = settings.getLastGameDate();
        if (lastGameDate == null) {
            return " - ";
        } else {
            String str_date = lastGameDate.toLocalDate().toString();
            String str_time = lastGameDate.getHourOfDay() + ":" + lastGameDate.getMinuteOfHour() + ":" + lastGameDate.getSecondOfMinute();
            return String.format("%s - %s", str_date, str_time);
        }
    }

    private void selectCurrentGameDifficultyInView() {
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

}
