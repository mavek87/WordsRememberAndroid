package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryVocableEditorView extends PojoManipulable<Word> {

    void saveVocableAction();

    void createTranslationAction();

    void goToTranslationEditView(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);
}

