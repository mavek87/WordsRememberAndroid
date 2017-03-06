package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.ViewPojoUser;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public interface DictionaryVocableEditorView extends ViewPojoUser<Word>{

    void saveVocableAction();

    void createTranslationAction();

    void goToTranslationEditView(Word vocable);

    void returnToPreviousView();

    void showMessage(String message);
}

