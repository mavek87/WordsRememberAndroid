package com.matteoveroni.wordsremember.scene_dictionary.events.translation;

import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */
public class EventTranslationManipulationRequest {

    private final Word translationToManipulate;
    private final TypeOfManipulationRequest typeOfManipulation;

    public EventTranslationManipulationRequest(Word translationToManipulate, TypeOfManipulationRequest typeOfManipulation) {
        this.translationToManipulate = translationToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulationRequest getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public Word getTranslationToManipulate() {
        return translationToManipulate;
    }
}
