package com.matteoveroni.wordsremember;

import android.content.SharedPreferences;

import com.matteoveroni.wordsremember.settings.Settings;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * @author Matteo Veroni
 */

public class SettingsTest {

    @Mock
    private SharedPreferences sharedPreferences;
    private final Settings settings = new Settings(sharedPreferences);

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
