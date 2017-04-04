package com.matteoveroni.wordsremember.dictionary.events.vocable;

/**
 * Created by Matteo Veroni
 */

public class EventGetUniqueVocablesWithTranslationsCompleted {

    private final int numberOfCountResults;

    public EventGetUniqueVocablesWithTranslationsCompleted(int numberOfCountResults) {
        this.numberOfCountResults = numberOfCountResults;
    }

    public int getNumberOfUniqueVocablesWithTranslation() {
        return numberOfCountResults;
    }
}
