package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * @author Matteo Veroni
 */

public class EventAsyncVocableDeletionComplete {

    private final int numberOfRowsDeleted;

    public EventAsyncVocableDeletionComplete(int numberOfRowsDeleted) {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public int getNumberOfRowsDeleted() {
        return numberOfRowsDeleted;
    }
}
