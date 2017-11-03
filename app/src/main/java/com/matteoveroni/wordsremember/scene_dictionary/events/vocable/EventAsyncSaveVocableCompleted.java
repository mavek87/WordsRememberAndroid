package com.matteoveroni.wordsremember.scene_dictionary.events.vocable;

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
