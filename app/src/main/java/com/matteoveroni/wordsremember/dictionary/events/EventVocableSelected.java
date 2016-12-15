package com.matteoveroni.wordsremember.dictionary.events;

/**
 * Event used when a dictionary item is selected in a fragment, to inform the main activity
 *
 * @author Matteo Veroni
 */

public class EventVocableSelected {

    private final long dictionaryItemIDSelected;

    public EventVocableSelected(long dictionaryItemIDSelected) {
        this.dictionaryItemIDSelected = dictionaryItemIDSelected;
    }

    public long getSelectedVocableID() {
        return dictionaryItemIDSelected;
    }
}
