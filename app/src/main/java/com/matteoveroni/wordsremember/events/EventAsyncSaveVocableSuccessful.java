package com.matteoveroni.wordsremember.events;

public class EventAsyncSaveVocableSuccessful {
    private final long insertedVocableId;

    public EventAsyncSaveVocableSuccessful(long insertedVocableId) {
        this.insertedVocableId = insertedVocableId;
    }

    public long getIdOfInsertedVocable() {
        return insertedVocableId;
    }
}
