package com.matteoveroni.wordsremember.dictionary.events;

import com.matteoveroni.wordsremember.pojo.Word;

public class EventSaveVocableRequest {

    private final Word vocable;

    public EventSaveVocableRequest(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocable() {
        return vocable;
    }
}
