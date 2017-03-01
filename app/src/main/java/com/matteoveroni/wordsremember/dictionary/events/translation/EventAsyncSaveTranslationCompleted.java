package com.matteoveroni.wordsremember.dictionary.events.translation;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveTranslationCompleted {

    private final long savedTranslationId;

    public EventAsyncSaveTranslationCompleted(long savedTranslationId) {
        this.savedTranslationId = savedTranslationId;
    }

    public long getSavedTranslationId() {
        return savedTranslationId;
    }
}
