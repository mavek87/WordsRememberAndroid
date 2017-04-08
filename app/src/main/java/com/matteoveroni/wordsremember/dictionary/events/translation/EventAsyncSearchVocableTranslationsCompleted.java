package com.matteoveroni.wordsremember.dictionary.events.translation;

import com.matteoveroni.wordsremember.dictionary.pojos.Word;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableTranslationsCompleted {

    private final Word vocable;
    private final List<Word> translations;

    public EventAsyncSearchVocableTranslationsCompleted(Word vocable, List<Word> translations) {
        this.vocable = vocable;
        this.translations = translations;
    }

    public Word getVocable() {
        return vocable;
    }

    public List<Word> getTranslations() {
        return translations;
    }
}
