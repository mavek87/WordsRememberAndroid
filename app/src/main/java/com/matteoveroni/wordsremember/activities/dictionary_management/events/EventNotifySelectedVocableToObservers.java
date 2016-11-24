package com.matteoveroni.wordsremember.activities.dictionary_management.events;

import com.matteoveroni.wordsremember.model.Word;

/**
 * @author Matteo Veroni
 */
public class EventNotifySelectedVocableToObservers {

    private final Word selectedVocable;

    public EventNotifySelectedVocableToObservers(Word selectedVocable) {
        this.selectedVocable = selectedVocable;
    }

    public Word getSelectedVocable() {
        return selectedVocable;
    }
}

