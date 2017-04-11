package com.matteoveroni.wordsremember.dictionary.events.vocable_translations;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted {

    private final long vocableId;

    public EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted(long vocableId) {
        this.vocableId = vocableId;
    }

    public long getVocableWithTranslationFound() {
        return vocableId;
    }
}
