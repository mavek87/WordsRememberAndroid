package com.matteoveroni.wordsremember.dictionary.events.translation;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class EventTranslationSelected {

    private final Word selectedTranslation;

    public EventTranslationSelected(Word selectedTranslation) {
        this.selectedTranslation = selectedTranslation;
    }

    public Word getSelectedTranslation() {
        return selectedTranslation;
    }
}
