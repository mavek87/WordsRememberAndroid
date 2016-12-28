package com.matteoveroni.wordsremember.pojo;

import com.matteoveroni.wordsremember.utilities.Json;

public class Word {

    private long id = -1;
    private final String name;

    public Word(String name) {
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

}
