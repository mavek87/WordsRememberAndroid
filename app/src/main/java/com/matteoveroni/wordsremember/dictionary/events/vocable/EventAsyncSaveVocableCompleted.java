package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveVocableCompleted {

    private final long savedVocableId;

    public EventAsyncSaveVocableCompleted(long savedVocableId) {
        this.savedVocableId = savedVocableId;
    }

    public long getSavedVocableId() {
        return savedVocableId;
    }
}
