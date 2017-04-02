package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * Created by Matteo Veroni
 */

public class EventCountUniqueVocablesWithTranslationsCompleted {

    private final int numberOfCountResults;

    public EventCountUniqueVocablesWithTranslationsCompleted(int numberOfCountResults) {
        this.numberOfCountResults = numberOfCountResults;
    }

    public int getNumberOfUniqueVocablesWithTranslation() {
        return numberOfCountResults;
    }
}
