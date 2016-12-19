package com.matteoveroni.wordsremember.dictionary.events;

public class EventAsyncDeleteVocable {

    private final int numberOfRowsDeleted;

    public EventAsyncDeleteVocable(int numberOfRowsDeleted) {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public int getNumberOfRowsDeleted() {
        return numberOfRowsDeleted;
    }
}
