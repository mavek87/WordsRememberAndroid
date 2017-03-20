package com.matteoveroni.wordsremember.dictionary.model;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    private Word lastValidVocableSelected;
    private Word vocableInView;

    public Word getLastValidVocableSelected() {
        return lastValidVocableSelected;
    }

    public void setLastValidVocableSelected(Word lastValidVocableSelected) {
        this.lastValidVocableSelected = lastValidVocableSelected;
    }

    public Word getEditedVocableInView() {
        return vocableInView;
    }

    public void setVocableInView(Word vocableInView) {
        this.vocableInView = vocableInView;
    }
}
