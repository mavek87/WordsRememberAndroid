package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.model.QuizGameSessionSettings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Matteo Veroni
 */

public class QuizGameSessionSettingsTest {

    private final QuizGameSessionSettings settings = new QuizGameSessionSettings();

    @Test
    public void test_setPositiveNumberOfQuestionShouldWorks() {
        int POSITIVE_NUMBER = 1;
        settings.setNumberOfQuestions(POSITIVE_NUMBER);
        assertEquals(POSITIVE_NUMBER, settings.getNumberOfQuestions());
    }

    @Test
    public void test_setZeroNumberOfQuestionsWorks() {
        int ZERO = 0;
        settings.setNumberOfQuestions(ZERO);
        assertEquals(ZERO, settings.getNumberOfQuestions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setNegativeNumberOfQuestionsThrowsException() {
        int NEGATIVE_NUMBER = -1;
        settings.setNumberOfQuestions(NEGATIVE_NUMBER);
    }
}
