package com.matteoveroni.wordsremember.settings.view;

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

}
