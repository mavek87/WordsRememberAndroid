package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;

/**
 * Created by Matteo Veroni
 */

public interface TranslationsSelectorView extends PojoManipulable<VocableTranslation> {

    void selectTranslationAction();

    void createTranslactionAction();

    void goToTranslationCreateView();

    void returnToPreviousView();

    void showMessage(String s);
}
