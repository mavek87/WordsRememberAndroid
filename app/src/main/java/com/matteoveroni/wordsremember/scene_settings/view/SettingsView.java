package com.matteoveroni.wordsremember.scene_settings.view;

import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface SettingsView extends View {

    void showDictionaryName(String dictionaryName);

    void showUsername(String username);

    void showDeviceLocale();

    void showLastGameDate(String lastGameDate);

    void showGameVersion(String gameVersion);

    void showGameDifficultyRadioButtonLabels();

    void toggleEasyDifficulty();

    void toggleMediumDifficulty();

    void toggleHardDifficulty();

    void onEasyDifficultySelected();

    void onMediumDifficultySelected();

    void onHardDifficultySelected();

    void onOnlineTranslationsCheckboxSelected();

    void checkOnlineTranslationsCheckPreference(boolean check);
}
