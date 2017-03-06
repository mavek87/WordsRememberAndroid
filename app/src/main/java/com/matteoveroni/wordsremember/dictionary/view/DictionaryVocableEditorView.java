package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.ViewPojoUser;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryVocableEditorView extends ViewPojoUser<Word>{

    void createTranslationAction();

    void goToTranslationsManipulationView(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);
}

