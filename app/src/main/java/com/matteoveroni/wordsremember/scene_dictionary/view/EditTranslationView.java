package com.matteoveroni.wordsremember.scene_dictionary.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;

/**
 * Created by Matteo Veroni
 */

public interface EditTranslationView extends View, PojoManipulable<VocableTranslation> {

    void saveTranslationAction();

    void returnToPreviousView();
}
