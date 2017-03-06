package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.ViewPojoUser;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * Created by Matteo Veroni
 */

public interface DictionaryTranslationEditorView extends ViewPojoUser<Word> {
    void saveTranslationAction();
}
