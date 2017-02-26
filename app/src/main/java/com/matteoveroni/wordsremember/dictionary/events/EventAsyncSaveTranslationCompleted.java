package com.matteoveroni.wordsremember.dictionary.events;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveTranslationCompleted {

    private final long savedVocableId;

    public EventAsyncSaveTranslationCompleted(long savedVocableId) {
        this.savedVocableId = savedVocableId;
    }

    public long getSavedVocableId() {
        return savedVocableId;
    }
}
