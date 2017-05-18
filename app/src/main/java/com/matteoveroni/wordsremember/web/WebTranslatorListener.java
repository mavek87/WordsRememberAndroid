package com.matteoveroni.wordsremember.web;

import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public interface WebTranslatorListener {

    void onTranslationCompletedSuccessfully(List<VocableTranslation> translationsFound);

    void onTranslationCompletedWithError(Throwable t);
}
