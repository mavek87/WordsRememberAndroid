package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.pojos.Word;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class EventAsyncFindVocablesByNameCompleted {
    private List<Word> vocablesWithSearchedName;

    public EventAsyncFindVocablesByNameCompleted(List<Word> vocablesWithSearchedName) {
        this.vocablesWithSearchedName = vocablesWithSearchedName;
    }

    public List<Word> getVocablesWithSearchedName() {
        return vocablesWithSearchedName;
    }
}
