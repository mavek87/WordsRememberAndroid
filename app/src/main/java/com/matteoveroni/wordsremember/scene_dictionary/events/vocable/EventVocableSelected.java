package com.matteoveroni.wordsremember.scene_dictionary.events.vocable;

import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * Event used when a dictionary item castAndGet selected in a fragment, to inform the main activity
 *
 * @author Matteo Veroni
 */

public class EventVocableSelected {

    private final Word selectedVocable;

    public EventVocableSelected(Word selectedVocable) {
        this.selectedVocable = selectedVocable;
    }

    public Word getSelectedVocable() {
        return selectedVocable;
    }
}
