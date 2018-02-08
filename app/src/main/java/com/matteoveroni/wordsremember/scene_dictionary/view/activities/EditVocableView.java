package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface EditVocableView extends View, PojoManipulable<Word> {

    void refresh();

    void saveVocableAction();

    void addTranslationAction();

    void returnToPreviousView();

    void showErrorDialogVocableNotSaved();

    void dismissErrorDialog();
}

