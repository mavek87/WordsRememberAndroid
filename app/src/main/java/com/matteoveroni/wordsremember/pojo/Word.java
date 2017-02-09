package com.matteoveroni.wordsremember.pojo;

import com.matteoveroni.wordsremember.utilities.Json;

public class Word {

    private long id = -1;
    private final String name;

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

    @Override
    public String toString() {
        return Json.getInstance().toJson(this);
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
}
