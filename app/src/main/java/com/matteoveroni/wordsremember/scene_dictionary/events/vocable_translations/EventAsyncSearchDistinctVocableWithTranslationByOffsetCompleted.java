package com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations;

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
