package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelTest {

    private QuizGameModel model;

    private static final int NEGATIVE_NUMBER_OF_QUIZZES = -1;
    private static final int NUMBER_OF_QUIZZES_IS_ZERO = 0;
    private static final int POSITIVE_NUMBER_OF_QUIZZES = 10;

    @Test(expected = IllegalArgumentException.class)
    public void test_AtTheBeginning_ifNumberOfQuestions_IsNegative_ThrowIllegalArgumentException() {
//        model = new QuizGameModel(NEGATIVE_NUMBER_OF_QUIZZES);
    }

    @Test
    public void test_AtTheBeginning_getRemainingNumberOfQuestions_IsEqualTo_numberOfQuestions() {
//        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUIZZES);
//        assertEquals(
//                "remainingNumberOfQuestions should be equal to numberOfQuestions",
//                POSITIVE_NUMBER_OF_QUIZZES, model.getRemainingNumberOfQuizzes()
//        );
    }

    @Test(expected = NoMoreQuizzesException.class)
    public void test_IfNoMoreQuestiongRemaining_getNextQuestionThrow_NoMoreQuestionsException() throws NoMoreQuizzesException {
//        model = new QuizGameModel(NUMBER_OF_QUIZZES_IS_ZERO);
//
//        model.getNextQuiz();
    }

    @Test
    public void test_IfOtherQuestionsRemains_getNextQuestionReturnsVocableTranslation() throws NoMoreQuizzesException {
//        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUIZZES);
//
//        assertNotNull(model.getNextQuiz());
    }

    @Test
    public void test_If_getNextQuestion_Called_getRemainingNumberOfQuestions_DecreaseByOne() throws NoMoreQuizzesException {
//        model = new QuizGameModel(POSITIVE_NUMBER_OF_QUIZZES);
//
//        int initialRemainingNumberOfQuestions = model.getRemainingNumberOfQuizzes();
//        model.getNextQuiz();
//        assertEquals("after getting the next question, the number of remaining questions should decrease by one",
//                (initialRemainingNumberOfQuestions - 1), model.getRemainingNumberOfQuizzes()
//        );
    }
}