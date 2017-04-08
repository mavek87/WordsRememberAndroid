package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * Created by Matteo Veroni
 */

public interface AddTranslation extends View, PojoManipulable<Word> {

    void createTranslationAction();

    void goToEditTranslationView();

    void returnToPreviousView();
}
