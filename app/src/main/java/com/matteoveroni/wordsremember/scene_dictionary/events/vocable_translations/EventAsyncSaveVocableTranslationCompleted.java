package com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSaveVocableTranslationCompleted {

    private final long idOfSavedTranslation;
    private final long idOfVocableTranslated;

    public EventAsyncSaveVocableTranslationCompleted(long idOfSavedTranslation, long idOfVocableTranslated) {
        this.idOfSavedTranslation = idOfSavedTranslation;
        this.idOfVocableTranslated = idOfVocableTranslated;
    }

    public long getIdOfSavedTranslation() {
        return idOfSavedTranslation;
    }

    public long getIdOfVocableTranslated() {
        return idOfVocableTranslated;
    }
}
