package com.matteoveroni.wordsremember.scene_dictionary.events.translation;

import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

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
