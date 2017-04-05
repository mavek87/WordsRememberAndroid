package com.matteoveroni.wordsremember.dictionary.events.vocable_translations;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableWithTranslationByOffsetCompleted {

    private final Word vocable;

    public EventAsyncSearchVocableWithTranslationByOffsetCompleted(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocableWithTranslationFound() {
        return vocable;
    }
}
