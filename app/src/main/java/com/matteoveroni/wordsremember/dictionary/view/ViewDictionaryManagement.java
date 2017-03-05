package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface ViewDictionaryManagement {

    void createVocableAction();

    void goToManipulationView(Word vocableToManipulate);

    void showMessage(String message);
}
