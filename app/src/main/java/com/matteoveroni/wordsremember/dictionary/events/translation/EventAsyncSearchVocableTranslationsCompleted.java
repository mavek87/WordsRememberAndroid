package com.matteoveroni.wordsremember.dictionary.events.translation;

import com.matteoveroni.wordsremember.pojos.Word;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class EventAsyncSearchVocableTranslationsCompleted {

    private final List<Word> translations;

    public EventAsyncSearchVocableTranslationsCompleted(List<Word> translations) {
        this.translations = translations;
    }

    public List<Word> getTranslations() {
        return translations;
    }
}
