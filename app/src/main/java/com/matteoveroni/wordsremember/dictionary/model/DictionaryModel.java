package com.matteoveroni.wordsremember.dictionary.model;

import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    private Word selectedVocable;

    public Word getSelectedVocable() {
        return selectedVocable;
    }

    public void setSelectedVocable(Word selectedVocable) {
        this.selectedVocable = selectedVocable;
    }
}
