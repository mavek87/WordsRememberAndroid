package com.matteoveroni.wordsremember.pojo;

import com.matteoveroni.wordsremember.utilities.Json;

public class Word {

    private final long id;
    private final String name;

    public Word(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return Json.getInstance().toJson(this);
    }

}
