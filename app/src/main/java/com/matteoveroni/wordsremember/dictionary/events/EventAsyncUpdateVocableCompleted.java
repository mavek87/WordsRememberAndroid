package com.matteoveroni.wordsremember.dictionary.events;

public class EventAsyncUpdateVocableCompleted {

    private final int numberOfVocablesUpdated;

    public EventAsyncUpdateVocableCompleted(int numberOfVocablesInvolved) {
        this.numberOfVocablesUpdated = numberOfVocablesInvolved;
    }

    public int getNumberOfVocablesUpdated() {
        return numberOfVocablesUpdated;
    }
}
