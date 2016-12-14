package com.matteoveroni.wordsremember.dictionary.events;

import com.matteoveroni.wordsremember.pojo.Word;

public class EventAsyncGetVocableByIdSuccessful {
    private final Word vocable;

    public EventAsyncGetVocableByIdSuccessful(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocableRetrieved() {
        return vocable;
    }
}
