package com.matteoveroni.wordsremember.dictionary.events.vocable_translations;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableWithTranslationByOffsetCompleted {

    private final long vocableId;

    public EventAsyncSearchVocableWithTranslationByOffsetCompleted(long vocableId) {
        this.vocableId = vocableId;
    }

    public long getVocableWithTranslationFound() {
        return vocableId;
    }
}
