package com.matteoveroni.wordsremember.dictionary.events;

/**
 * @author Matteo Veroni
 */

public class EventAsyncDeleteVocableCompleted {

    private final int numberOfRowsDeleted;

    public EventAsyncDeleteVocableCompleted(int numberOfRowsDeleted) {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public int getNumberOfRowsDeleted() {
        return numberOfRowsDeleted;
    }
}
