package com.matteoveroni.wordsremember.dictionary.events;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveVocableCompleted {
    private final long insertedVocableId;
    private String errorMessage;

    public EventAsyncSaveVocableCompleted(long insertedVocableId) {
        this.insertedVocableId = insertedVocableId;
    }

    public long getIdOfSavedVocable() {
        return insertedVocableId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean anErrorOccurred(){
        return insertedVocableId < 0;
    }
}
