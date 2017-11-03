package com.matteoveroni.wordsremember.scene_settings.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface SettingsView extends View {

    void toggleEasyDifficulty();

    void toggleMediumDifficulty();

    void toggleHardDifficulty();

    void easyDifficultySelected();

    void mediumDifficultySelected();

    void hardDifficultySelected();

    void onlineTranslationsCheckSelected();

    void checkOnlineTranslationsCheckPreference(boolean check);

    void setLastGameDate(FormattedString lastGameDate);
}
