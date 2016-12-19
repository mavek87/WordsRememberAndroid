package com.matteoveroni.wordsremember.dictionary.events;

public class EventAsyncUpdateVocable {

    private final int numberOfVocablesUpdated;

    public EventAsyncUpdateVocable(int numberOfVocablesUpdated) {
        this.numberOfVocablesUpdated = numberOfVocablesUpdated;
    }

    public int getNumberOfVocablesUpdated() {
        return numberOfVocablesUpdated;
    }
}
