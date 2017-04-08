package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;

/**
 * Created by Matteo Veroni
 */

public interface EditTranslation extends View, PojoManipulable<VocableTranslation> {

    void saveTranslationAction();

    void returnToPreviousView();
}
