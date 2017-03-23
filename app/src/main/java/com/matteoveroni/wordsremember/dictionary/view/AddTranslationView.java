package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulableView;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * Created by Matteo Veroni
 */

public interface AddTranslationView extends PojoManipulableView<VocableTranslation> {

    void createTranslationAction();

    void goToEditTranslationView(Word vocable);

    void returnToPreviousView();

    void showMessage(String s);
}
