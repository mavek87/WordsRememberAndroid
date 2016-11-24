package com.matteoveroni.wordsremember.activities.dictionary_management.events;

import com.matteoveroni.wordsremember.model.Word;

/**
 * @author Matteo Veroni
 */
public class EventInformObserversOfItemSelected {

    private final Word itemSelected;

    public EventInformObserversOfItemSelected(Word itemSelected) {
        this.itemSelected = itemSelected;
    }

    public Word getItemSelected() {
        return itemSelected;
    }
}

