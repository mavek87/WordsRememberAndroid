package com.matteoveroni.wordsremember.scene_dictionary.events.vocable;

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
