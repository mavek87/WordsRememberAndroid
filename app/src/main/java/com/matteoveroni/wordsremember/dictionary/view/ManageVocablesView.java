package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface ManageVocablesView {

    void createVocableAction();

    void goToEditVocableView();

    void showMessage(String message);

}
