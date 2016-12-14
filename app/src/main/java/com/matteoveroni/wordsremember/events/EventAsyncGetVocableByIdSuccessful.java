package com.matteoveroni.wordsremember.events;

import com.matteoveroni.wordsremember.models.Word;

public class EventAsyncGetVocableByIdSuccessful {
    private final Word vocable;

    public EventAsyncGetVocableByIdSuccessful(Word vocable) {
        this.vocable = vocable;
    }

    public Word getVocableRetrieved() {
        return vocable;
    }
}
