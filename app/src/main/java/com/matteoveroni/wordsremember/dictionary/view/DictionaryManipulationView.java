package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryManipulationView {

    void showVocableData(Word vocable);

    void createTranslationAction();

    void goToTranslationsManipulationView(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);
}

