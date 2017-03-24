package com.matteoveroni.wordsremember.pojos;

/**
 * @author Matteo Veroni
 */

// TODO: check if is possible to remove empty translation (if needed reimplement equals method)
public class VocableTranslation {
    private final Word vocable;
    private Word translation = new Word("");

    public VocableTranslation(Word vocable) {
        this.vocable = vocable;
    }

    public VocableTranslation(Word vocable, Word translation) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VocableTranslation that = (VocableTranslation) o;

        if (vocable != null ? !vocable.equals(that.vocable) : that.vocable != null) return false;
        return translation != null ? translation.equals(that.translation) : that.translation == null;

    }

    @Override
    public int hashCode() {
        int result = vocable != null ? vocable.hashCode() : 0;
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
        return result;
    }
}
