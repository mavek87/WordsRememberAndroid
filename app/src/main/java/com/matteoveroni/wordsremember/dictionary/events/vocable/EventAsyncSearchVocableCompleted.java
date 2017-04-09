package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableCompleted {
    private final Word vocable;

    public EventAsyncSearchVocableCompleted(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocable() {
        return vocable;
    }
}
