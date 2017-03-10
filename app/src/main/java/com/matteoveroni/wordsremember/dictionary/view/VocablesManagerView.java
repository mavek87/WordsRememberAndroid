package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface VocablesManagerView {

    void createVocableAction();

    void goToVocableEditView(Word vocableToEdit);

    void showMessage(String message);

}
