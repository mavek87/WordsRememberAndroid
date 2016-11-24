package com.matteoveroni.wordsremember.activities.dictionary_management.events;

/**
 * Event used when a dictionary item is selected in a fragment, to inform the main activity
 *
 * @author Matteo Veroni
 */

public class EventDictionaryItemSelected {

    private final long dictionaryItemIDSelected;

    public EventDictionaryItemSelected(long dictionaryItemIDSelected) {
        this.dictionaryItemIDSelected = dictionaryItemIDSelected;
    }

    public long getDictionaryItemIDSelected() {
        return dictionaryItemIDSelected;
    }
}
