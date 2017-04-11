package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * Created by Matteo Veroni
 */

public class EventCountDistinctVocablesWithTranslationsCompleted {

    private final int numberOfCountResults;

    public EventCountDistinctVocablesWithTranslationsCompleted(int numberOfCountResults) {
        this.numberOfCountResults = numberOfCountResults;
    }

    public int getNumberOfUniqueVocablesWithTranslation() {
        return numberOfCountResults;
    }
}
