package com.matteoveroni.wordsremember.dictionary.events;

public class EventVocableUpdated {

    private final int numberOfVocablesUpdated;

    public EventVocableUpdated(int numberOfVocablesInvolved) {
        this.numberOfVocablesUpdated = numberOfVocablesInvolved;
    }

    public int getNumberOfVocablesUpdated() {
        return numberOfVocablesUpdated;
    }
}
