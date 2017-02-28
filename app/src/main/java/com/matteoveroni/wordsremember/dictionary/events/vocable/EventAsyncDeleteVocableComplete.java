package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * @author Matteo Veroni
 */

public class EventAsyncDeleteVocableComplete {

    private final int numberOfRowsDeleted;

    public EventAsyncDeleteVocableComplete(int numberOfRowsDeleted) {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public int getNumberOfRowsDeleted() {
        return numberOfRowsDeleted;
    }
}
