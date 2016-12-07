package com.matteoveroni.wordsremember.events;

/**
 * @author Matteo Veroni
 */
public class EventManipulateVocable {

    private final long vocableIDToManipulte;
    private final TypeOfManipulation typeOfManipulation;

    public enum TypeOfManipulation {
        EDIT, REMOVE;
    }

    public EventManipulateVocable(long vocableIDToManipulate, TypeOfManipulation typeOfManipulation) {
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
