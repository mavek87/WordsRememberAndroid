package com.matteoveroni.wordsremember.dictionary.events.vocable_translations;

import com.matteoveroni.wordsremember.dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */
public class EventVocableTranslationManipulationRequest {

    private final Word vocableToManipulate;
    private final Word translationToManipulate;
    private final TypeOfManipulationRequest typeOfManipulation;

    public EventVocableTranslationManipulationRequest(Word vocableToManipulate, Word translationToManipulate, TypeOfManipulationRequest typeOfManipulation) {
        this.vocableToManipulate = vocableToManipulate;
        this.translationToManipulate = translationToManipulate;
        this.typeOfManipulation = typeOfManipulation;
    }

    public TypeOfManipulationRequest getTypeOfManipulation() {
        return typeOfManipulation;
    }

    public Word getVocableToManipulate() {
        return vocableToManipulate;
    }

    public Word getTranslationToManipulate() {
        return translationToManipulate;
    }


}
