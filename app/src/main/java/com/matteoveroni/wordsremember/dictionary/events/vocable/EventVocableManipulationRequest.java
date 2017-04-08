package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */
public class EventVocableManipulationRequest {

    private final Word vocableToManipulate;
    private final TypeOfManipulationRequest typeOfManipulation;

    public EventVocableManipulationRequest(Word vocableToManipulate, TypeOfManipulationRequest typeOfManipulation) {
        this.vocableToManipulate = vocableToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulationRequest getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public Word getVocableToManipulate() {
        return vocableToManipulate;
    }
}
