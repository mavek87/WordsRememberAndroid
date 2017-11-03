package com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations;

import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;

/**
 * @author Matteo Veroni
 */
public class EventVocableTranslationManipulationRequest {

    private final long vocableIdToManipulate;
    private final long translationIdToManipulate;
    private final TypeOfManipulationRequest typeOfManipulation;

    public EventVocableTranslationManipulationRequest(long vocableIdToManipulate, long translationIdToManipulate, TypeOfManipulationRequest typeOfManipulation) {
        this.vocableIdToManipulate = vocableIdToManipulate;
        this.translationIdToManipulate = translationIdToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulationRequest getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public long getVocableIdToManipulate() {
        return vocableIdToManipulate;
    }

    public long getTranslationIdToManipulate() {
        return translationIdToManipulate;
    }


}
