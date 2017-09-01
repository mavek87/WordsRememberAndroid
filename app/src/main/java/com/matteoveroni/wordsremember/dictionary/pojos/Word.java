package com.matteoveroni.wordsremember.dictionary.pojos;

import com.matteoveroni.myutils.Json;

/**
 * @author Matteo Veroni
 */

public class Word {

    private long id = -1;
    private String name = "";

    public Word() {
    }

    public Word(String name) {
        this.name = name;
    }

    public Word(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        return Json.getInstance().toJson(this);
    }

    public static Word fromJson(String json) {
        return Json.getInstance().fromJson(json, Word.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word = (Word) o;

        return getId() == word.getId() && getName().equals(word.getName());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getName().hashCode();
        return result;
    }

    public static final boolean isValid(Word word) {
        return word != null && word.getName() != null && !word.getName().trim().isEmpty();
    }
}
