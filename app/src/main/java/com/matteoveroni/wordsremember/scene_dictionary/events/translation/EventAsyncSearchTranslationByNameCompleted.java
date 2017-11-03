package com.matteoveroni.wordsremember.scene_dictionary.events.translation;

import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

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
