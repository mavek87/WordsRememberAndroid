package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface EditVocableView extends PojoManipulable<Word> {

    void saveVocableAction();

    void addTranslationAction();

    void goToTranslationSelectorView(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);

}

