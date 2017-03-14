package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * Created by Matteo Veroni
 */

public interface AddTranslationView extends PojoManipulable<VocableTranslation> {

    void selectTranslationAction();

    void createTranslactionAction();

    void goToEditTranslationView(Word vocable);

    void returnToPreviousView();

    void showMessage(String s);
}
