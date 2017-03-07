package com.matteoveroni.wordsremember.pojos;

/**
 * @author Matteo Veroni
 */

public class TranslationForVocable {
    private final Word vocable;
    private Word translation = new Word("");

    public TranslationForVocable(Word vocable) {
        this.vocable = vocable;
    }

    public TranslationForVocable(Word vocable, Word translation) {
        this.vocable = vocable;
        this.translation = translation;
    }

    public Word getVocable() {
        return vocable;
    }

    public Word getTranslation() {
        return translation;
    }

    public void setTranslation(Word translation) {
        this.translation = translation;
    }
}
