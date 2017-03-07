package com.matteoveroni.wordsremember.dictionary.view;

import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.TranslationForVocable;

/**
 * Created by Matteo Veroni
 */

public interface DictionaryTranslationEditor extends PojoManipulable<TranslationForVocable> {
    void saveTranslationAction();
}
