package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojo.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryManipulationView {

    void showVocableData(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);
}

