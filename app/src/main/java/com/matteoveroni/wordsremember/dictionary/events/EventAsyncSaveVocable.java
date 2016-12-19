package com.matteoveroni.wordsremember.dictionary.events;

public class EventAsyncSaveVocable {
    private final long insertedVocableId;

    public EventAsyncSaveVocable(long insertedVocableId) {
        this.insertedVocableId = insertedVocableId;
    }

    public long getIdOfInsertedVocable() {
        return insertedVocableId;
    }
}
