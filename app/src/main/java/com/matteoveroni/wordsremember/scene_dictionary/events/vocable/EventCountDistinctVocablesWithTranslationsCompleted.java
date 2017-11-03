package com.matteoveroni.wordsremember.scene_dictionary.events.vocable;

/**
 * Created by Matteo Veroni
 */

public class EventCountDistinctVocablesWithTranslationsCompleted {

    private final int numberOfCountResults;

    public EventCountDistinctVocablesWithTranslationsCompleted(int numberOfCountResults) {
        this.numberOfCountResults = numberOfCountResults;
    }

    public int getNumberOfVocablesWithTranslation() {
        return numberOfCountResults;
    }
}
