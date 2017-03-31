package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuestionsException;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelTest {

    private QuizGameModel model;

    private static final int NEGATIVE_NUMBER_OF_QUESTIONS = -1;
    private static final int NUMBER_OF_QUESTIONS_IS_ZERO = 0;
    private static final int POSITIVE_NUMBER_OF_QUESTIONS = 10;

    @Test(expected = IllegalArgumentException.class)
    public void test_AtTheBeginning_ifNumberOfQuestions_IsNegative_ThrowIllegalArgumentException() {
        model = new QuizGameModel(NEGATIVE_NUMBER_OF_QUESTIONS);
    }

    @Test
    public void test_AtTheBeginning_getRemainingNumberOfQuestions_IsEqualTo_numberOfQuestions() {
        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUESTIONS);
        assertEquals(
                "remainingNumberOfQuestions should be equal to numberOfQuestions",
                POSITIVE_NUMBER_OF_QUESTIONS, model.getRemainingNumberOfQuestions()
        );
    }

    @Test(expected = NoMoreQuestionsException.class)
    public void test_IfNoMoreQuestiongRemaining_getNextQuestionThrow_NoMoreQuestionsException() throws NoMoreQuestionsException {
        model = new QuizGameModel(NUMBER_OF_QUESTIONS_IS_ZERO);

        model.getNextQuestion();
    }

    @Test
    public void test_IfOtherQuestionsRemains_getNextQuestionReturnsVocableTranslation() throws NoMoreQuestionsException {
        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUESTIONS);

        assertNotNull(model.getNextQuestion());
    }

    @Test
    public void test_If_getNextQuestion_Called_getRemainingNumberOfQuestions_DecreaseByOne() throws NoMoreQuestionsException {
        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUESTIONS);

        int initialRemainingNumberOfQuestions = model.getRemainingNumberOfQuestions();
        model.getNextQuestion();
        assertEquals("after getting the next question, the number of remaining questions should decrease by one",
                (initialRemainingNumberOfQuestions - 1), model.getRemainingNumberOfQuestions()
        );
    }


//
//
//
//
//    @Test
//    public void test_getNextQuestionReturnsNoTranslationsIfTheyAreFinished() {
//        final int numberOfQuestions = 10;
//        final int numberOfTranslationsForVocables = 1;
//        model = new QuizGameModel(numberOfQuestions, numberOfTranslationsForVocables);
//        VocableTranslation returnedVocableTranslation = model.getNextQuestion();
//        assertNotNull(returnedVocableTranslation);
//    }

}