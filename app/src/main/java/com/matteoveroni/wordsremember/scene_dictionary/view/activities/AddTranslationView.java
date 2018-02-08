package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * Created by Matteo Veroni
 */

public interface AddTranslationView extends View, PojoManipulable<Word> {

    void createTranslationAction();

    void returnToPreviousView();
}
