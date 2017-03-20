package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.pojos.Word;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
