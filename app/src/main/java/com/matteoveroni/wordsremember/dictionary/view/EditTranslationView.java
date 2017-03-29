package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulableView;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;

/**
 * Created by Matteo Veroni
 */

public interface EditTranslationView extends PojoManipulableView<VocableTranslation> {

    void saveTranslationAction();

    void returnToPreviousView();

    void showMessage(String s);
}
