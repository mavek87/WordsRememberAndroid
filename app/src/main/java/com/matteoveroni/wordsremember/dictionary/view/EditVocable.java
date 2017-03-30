package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface EditVocable extends View, PojoManipulable<Word> {

    void saveVocableAction();

    void addTranslationAction();

    void goToAddTranslationView();

    void returnToPreviousView();

    void showDialogCannotAddTranslationIfVocableNotSaved();
}

