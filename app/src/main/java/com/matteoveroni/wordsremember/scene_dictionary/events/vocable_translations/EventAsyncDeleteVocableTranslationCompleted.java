package com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations;

/**
 * @author Matteo Veroni
 */

public class EventAsyncDeleteVocableTranslationCompleted {

    private final int numberOfRowsDeleted;

    public EventAsyncDeleteVocableTranslationCompleted(int numberOfRowsDeleted) {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public int getNumberOfRowsDeleted() {
        return numberOfRowsDeleted;
    }
}
