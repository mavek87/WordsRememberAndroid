package com.matteoveroni.wordsremember.dictionary.events.vocable;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */
public class EventVocableManipulationRequest {

    private final Word vocableToManipulate;
    private final TypeOfManipulation typeOfManipulation;

    public enum TypeOfManipulation {
        REMOVE;
    }

    public EventVocableManipulationRequest(Word vocableToManipulate, TypeOfManipulation typeOfManipulation) {
        this.vocableToManipulate = vocableToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulation getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public Word getVocableToManipulate() {
        return vocableToManipulate;
    }
}
