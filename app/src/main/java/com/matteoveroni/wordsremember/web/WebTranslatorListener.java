package com.matteoveroni.wordsremember.web;

import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public interface WebTranslatorListener {

    void onTranslationCompletedSuccessfully(List<Word> translationsFound);

    void onTranslationCompletedWithError(Throwable t);
}
