package com.matteoveroni.wordsremember.dictionary.events;

import com.matteoveroni.wordsremember.pojo.Word;

public class EventAsyncGetVocableById {
    private final Word vocable;

    public EventAsyncGetVocableById(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocableRetrieved() {
        return vocable;
    }
}
