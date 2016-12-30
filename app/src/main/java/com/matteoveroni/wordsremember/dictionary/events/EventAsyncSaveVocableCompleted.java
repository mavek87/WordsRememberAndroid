package com.matteoveroni.wordsremember.dictionary.events;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveVocableCompleted {
    private final long insertedVocableId;

    public EventAsyncSaveVocableCompleted(long insertedVocableId) {
        this.insertedVocableId = insertedVocableId;
    }

    public long getIdOfSavedVocable() {
        return insertedVocableId;
    }
}
