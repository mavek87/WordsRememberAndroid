package com.matteoveroni.wordsremember.dictionary.events;

/**
 * @author Matteo Veroni
 */
public class EventVocableManipulationRequest {

    private final long vocableIDToManipulte;
    private final TypeOfManipulation typeOfManipulation;

    public enum TypeOfManipulation {
        EDIT, REMOVE;
    }

    public EventVocableManipulationRequest(long vocableIDToManipulate, TypeOfManipulation typeOfManipulation) {
        this.vocableIDToManipulte = vocableIDToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulation getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public long getVocableIDToManipulate() {
        return vocableIDToManipulte;
    }
}
