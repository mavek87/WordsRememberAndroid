package com.matteoveroni.wordsremember.events;

import com.matteoveroni.wordsremember.pojo.Word;

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

