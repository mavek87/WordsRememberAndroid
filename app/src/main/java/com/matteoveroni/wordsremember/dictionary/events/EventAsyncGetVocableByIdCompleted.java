package com.matteoveroni.wordsremember.dictionary.events;

import com.matteoveroni.wordsremember.pojo.Word;

public class EventAsyncGetVocableByIdCompleted {
    private final Word vocable;

    public EventAsyncGetVocableByIdCompleted(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocableRetrieved() {
        return vocable;
    }
}
