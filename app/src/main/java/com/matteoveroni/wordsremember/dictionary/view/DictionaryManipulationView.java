package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojo.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryManipulationView {

    void showMessage(String message);

    void returnToPreviousView();

    void populateViewForVocable(Word vocable);
}

