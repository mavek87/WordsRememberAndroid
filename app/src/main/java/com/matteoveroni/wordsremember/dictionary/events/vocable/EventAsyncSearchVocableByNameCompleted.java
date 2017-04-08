package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableByNameCompleted {
    private final Word vocablesWithSearchedName;

    public EventAsyncSearchVocableByNameCompleted(Word vocablesWithSearchedName) {
        this.vocablesWithSearchedName = vocablesWithSearchedName;
    }

    public Word getVocableWithSearchedName() {
        return vocablesWithSearchedName;
    }
}
