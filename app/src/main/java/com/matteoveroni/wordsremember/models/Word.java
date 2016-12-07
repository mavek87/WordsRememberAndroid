package com.matteoveroni.wordsremember.models;

import com.matteoveroni.wordsremember.utilities.Json;

public class Word {

    private final String name;

    public Word(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object otherWordObject) {
        if (this == otherWordObject) return true;
        if (!(otherWordObject instanceof Word)) return false;
        Word otherWord = (Word) otherWordObject;
        return getName() != null ? getName().equals(otherWord.getName()) : otherWord.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return Json.getInstance().toJson(this);
    }

}
