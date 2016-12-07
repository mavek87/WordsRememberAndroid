package com.matteoveroni.wordsremember.models;

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DictionaryTest {

    private static String DICTIONARY_NAME = "italianEnglishDictionary";
    private Dictionary dictionary;

    private final Word wordCane = new Word("cane");
    private final Word wordDog = new Word("dog");
    private final Word wordImpaurire = new Word("impaurire");
    private final Word wordFrighten = new Word("frighten");
    private final Word wordScare = new Word("scare");

    @Before
    public void setUp() {
        dictionary = new Dictionary(DICTIONARY_NAME);
    }

    @Test
    public void testAddVocable() {
        String testName = "testAddVocable";

        String testError = " - dictionary is not empty";
        assertFalse(testName + testError, dictionary.containsVocable(wordCane));

        dictionary.addVocable(wordCane);

        testError = " - word added is not being really added";
        assertTrue(testName + testError, dictionary.containsVocable(wordCane));
    }

    @Test
    public void testAddVocables() {
        String testName = "testAddVocables";

        dictionary.addVocables(wordImpaurire, wordFrighten, wordScare);

        String testError = " - some vocable is not being added correctly";
        assertTrue(testName + testError, dictionary.containsVocable(wordImpaurire));
        assertTrue(testName + testError, dictionary.containsVocable(wordFrighten));
        assertTrue(testName + testError, dictionary.containsVocable(wordScare));
    }

    @Test
    public void testRemoveVocable() {
        String testName = "testRemoveVocable";

        dictionary.addVocable(wordImpaurire);

        assertTrue(testName, dictionary.containsVocable(wordImpaurire));

        dictionary.removeVocable(new Word("impaurire"));

        String testError = " - word is not being deleted";
        assertFalse(testName + testError, dictionary.containsVocable(wordImpaurire));
    }
}
