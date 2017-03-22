package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulableView;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface EditVocableView extends PojoManipulableView<Word> {

    void saveVocableAction();

    void addTranslationAction();

    void goToAddTranslationView();

    void returnToPreviousView();

    void showMessage(String message);
}

