package com.matteoveroni.wordsremember.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    private final String name;
    private Map<Word, List<Word>> vocabulary;

    public Dictionary(String name) {
        this.name = name;
        vocabulary = new HashMap<>();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getName() {
        return name;
    }

    public List<Word> getTranslationsForVocable(Word vocable) {
        return (vocable != null ? vocabulary.get(vocable) : null);
    }

    public List<Word> getVocables() {
        return new ArrayList<>(vocabulary.keySet());
    }

    // Tested
    public boolean containsVocable(Word vocable) {
        return vocabulary.containsKey(vocable);
    }

    // Tested
    public void addVocable(Word word) {
        vocabulary.put(word, new ArrayList<Word>());
    }

    // Tested
    public void addVocables(Word... words) {
        for (Word word : words) {
            addVocable(word);
        }
    }

    public void addTranslationForVocable(Word translation, Word vocable) {
        if (vocabulary.containsKey(vocable)) {
            vocabulary.get(vocable).add(translation);
        } else {
            vocabulary.put(vocable, new ArrayList<>(Arrays.asList(translation)));
        }
    }

    public void addTranslationsForVocable(List<Word> translations, Word vocable) {
        if (vocabulary.containsKey(vocable)) {
            vocabulary.get(vocable).addAll(translations);
        } else {
            vocabulary.put(vocable, translations);
        }
    }

    public void addTranslationsForVocable(Word[] translations, Word vocable) {
        addTranslationsForVocable(Arrays.asList(translations), vocable);
    }

    // Tested
    public void removeVocable(Word word) {
        vocabulary.remove(word);
    }

    public void removeVocables(Word... words) {
        for (Word word : words) {
            removeVocable(word);
        }
    }

    public void removeTranslationForWord(Word translation, Word word) {
        if (vocabulary.containsKey(word)) {
            vocabulary.get(word).remove(translation);
        }
    }
}
