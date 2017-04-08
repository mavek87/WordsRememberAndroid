package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;

import org.junit.Test;

/**
 * @author Matteo Veroni
 */

public class QuizGameFindTranslationForVocableModelTest {

    private QuizGameFindTranslationForVocableModel model;

    private static final int NEGATIVE_NUMBER_OF_QUIZZES = -1;
    private static final int NUMBER_OF_QUIZZES_IS_ZERO = 0;
    private static final int POSITIVE_NUMBER_OF_QUIZZES = 10;

    @Test(expected = IllegalArgumentException.class)
    public void test_AtTheBeginning_ifNumberOfQuestions_IsNegative_ThrowIllegalArgumentException() {
//        model = new QuizGameFindTranslationForVocableModel(NEGATIVE_NUMBER_OF_QUIZZES);
    }

    @Test
    public void test_AtTheBeginning_getRemainingNumberOfQuestions_IsEqualTo_numberOfQuestions() {
//        model = new QuizGameFindTranslationForVocableModel(POSITIVE_NUMBER_OF_QUIZZES);
//        assertEquals(
//                "remainingNumberOfQuestions should be equal to numberOfQuestions",
//                POSITIVE_NUMBER_OF_QUIZZES, model.getRemainingNumberOfQuizzes()
//        );
    }

    @Test(expected = NoMoreQuizzesException.class)
    public void test_IfNoMoreQuestiongRemaining_getNextQuestionThrow_NoMoreQuestionsException() throws NoMoreQuizzesException {
//        model = new QuizGameFindTranslationForVocableModel(NUMBER_OF_QUIZZES_IS_ZERO);
//
//        model.getNextQuiz();
    }

    @Test
    public void test_IfOtherQuestionsRemains_getNextQuestionReturnsVocableTranslation() throws NoMoreQuizzesException {
//        model = new QuizGameFindTranslationForVocableModel(POSITIVE_NUMBER_OF_QUIZZES);
//
//        assertNotNull(model.getNextQuiz());
    }

    @Test
    public void test_If_getNextQuestion_Called_getRemainingNumberOfQuestions_DecreaseByOne() throws NoMoreQuizzesException {
//        model = new QuizGameFindTranslationForVocableModel(POSITIVE_NUMBER_OF_QUIZZES);
//
//        int initialRemainingNumberOfQuestions = model.getRemainingNumberOfQuizzes();
//        model.getNextQuiz();
//        assertEquals("after getting the next question, the number of remaining questions should decrease by one",
//                (initialRemainingNumberOfQuestions - 1), model.getRemainingNumberOfQuizzes()
//        );
    }
}