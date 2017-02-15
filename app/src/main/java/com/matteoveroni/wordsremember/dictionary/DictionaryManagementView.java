package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.pojo.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryManagementView {

    void createVocableAction();

    void goToManipulationView(Word vocableToManipulate);

    void showMessage(String message);
}
