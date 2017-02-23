package com.matteoveroni.wordsremember.dictionary.events;

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

    public EventVocableManipulationRequest(Word vocableIDToManipulate, TypeOfManipulation typeOfManipulation) {
        this.vocableToManipulate = vocableIDToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulation getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public Word getVocableToManipulate() {
        return vocableToManipulate;
    }
}
