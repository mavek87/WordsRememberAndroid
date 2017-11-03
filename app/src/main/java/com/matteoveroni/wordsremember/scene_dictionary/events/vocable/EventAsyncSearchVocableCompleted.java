package com.matteoveroni.wordsremember.scene_dictionary.events.vocable;

import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

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
