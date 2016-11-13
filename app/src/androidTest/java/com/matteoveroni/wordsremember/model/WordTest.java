package com.matteoveroni.wordsremember.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WordTest {

    @Test
    public void testWordsWithSameNameAreEquals() {
        String testName = "testWordsWithSameNameAreEquals";

        final Word word1 = new Word("sameName");
        final Word word2 = new Word("sameName");

        String testError = " - word with same name aren't considered equals";
        assertEquals(testName + testError, word1, word2);
    }

}
