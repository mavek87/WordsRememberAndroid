package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.model.QuizGameSessionSettings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Matteo Veroni
 */

public class QuizGameSessionSettingsTest {

    private final QuizGameSessionSettings settings = new QuizGameSessionSettings();

    private final int NEGATIVE_NUMBER = -1;
    private final int ZERO = 0;
    private final int POSITIVE_NUMBER = 1;

    @Test
    public void test_setPositiveNumberOfQuestionShouldWorks() {
        settings.setNumberOfQuestions(POSITIVE_NUMBER);
        assertEquals(POSITIVE_NUMBER, settings.getNumberOfQuestions());
    }

    @Test
    public void test_setZeroNumberOfQuestionsWorks() {
        settings.setNumberOfQuestions(ZERO);
        assertEquals(ZERO, settings.getNumberOfQuestions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setNegativeNumberOfQuestionsThrowsException() {
        settings.setNumberOfQuestions(NEGATIVE_NUMBER);
    }
}
