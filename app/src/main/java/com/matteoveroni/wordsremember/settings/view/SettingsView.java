package com.matteoveroni.wordsremember.settings.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.view.View;

import org.joda.time.DateTime;

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
