package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.pojos.Word;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableByNameCompleted {
    private final List<Word> vocablesWithSearchedName;

    public EventAsyncSearchVocableByNameCompleted(List<Word> vocablesWithSearchedName) {
        this.vocablesWithSearchedName = vocablesWithSearchedName;
    }

    public Word getVocableWithSearchedName() {
        if (vocablesWithSearchedName.size() == 1) {
            return vocablesWithSearchedName.get(0);
        }
        return null;
    }

    public boolean isUniqueVocableWithSearchedNamePresent() {
        return vocablesWithSearchedName.size() == 1;
    }
}
