package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;

/**
 * Created by Matteo Veroni
 */

public interface TranslationCreateView extends PojoManipulable<VocableTranslation> {

    void saveTranslationAction();

    void returnToPreviousView();

    void showMessage(String s);
}
