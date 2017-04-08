package com.matteoveroni.wordsremember.dictionary.events.translation;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchTranslationByNameCompleted {
    private final Word translationWithSearchedName;

    public EventAsyncSearchTranslationByNameCompleted(Word translationWithSearchedName) {
        this.translationWithSearchedName = translationWithSearchedName;
    }

    public Word getTranslationWithSearchedName() {
        return translationWithSearchedName;
    }
}
