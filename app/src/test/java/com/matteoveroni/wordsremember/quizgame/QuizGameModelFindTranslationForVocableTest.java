package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenterTest;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameSessionSettings;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenterFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Dictionary;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelFindTranslationForVocableTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private QuizGameSessionSettings settings;
    @Mock
    private DictionaryDAO dao;

//    private QuizGamePresenter presenter;


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

//    private class DictionaryAddTranslationPresenterFactoryForTests implements PresenterFactory {
//        private DictionaryDAO dao;
//
//        DictionaryAddTranslationPresenterFactoryForTests(DictionaryDAO dao) {
//            this.dao = dao;
//        }
//
//        @Override
//        public QuizGamePresenter create() {
//            return new QuizGamePresenter(dao);
//        }
//    }
}