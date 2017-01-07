package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojo.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryManagementView {

    void showMessage(String message);

    void goToManipulationView(Word vocableToManipulate);
}
