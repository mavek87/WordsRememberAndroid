package com.matteoveroni.wordsremember.events;

import com.matteoveroni.wordsremember.pojo.Word;

/**
 * @author Matteo Veroni
 */
public class EventVisualizeVocable {

    private final Word vocable;

    public EventVisualizeVocable(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocable() {
        return vocable;
    }
}

