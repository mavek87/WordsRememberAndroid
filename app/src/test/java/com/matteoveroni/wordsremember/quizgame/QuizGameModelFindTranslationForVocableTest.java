package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenterTest;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameSessionSettings;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenterFactory;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Dictionary;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private QuizGameModelFindTranslationForVocable model;

    @Before
    public void setUp() {
        model = new QuizGameModelFindTranslationForVocable(settings, dao);
    }

    @Test
    public void test_After_onEventCalculateNumberOfQuizzes_EventQuizModelInitialized() {
        int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 1;

        model.onEventCalculateNumberOfQuizzes(
                new EventCountDistinctVocablesWithTranslationsCompleted(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS)
        );

        EVENT_BUS.hasSubscriberForEvent(EventQuizModelInitialized.class);
    }

    @Test
    public void test_onEventCalculateNumberOfQuizzes_IfNumberOfQuestionsAreMoreThanPossibleQuestions_ReduceThem() {
        int NUMBER_OF_QUESTIONS_FROM_SETTINGS = 2;
        int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 1;

        when(settings.getNumberOfQuestions()).thenReturn(NUMBER_OF_QUESTIONS_FROM_SETTINGS);

        model.onEventCalculateNumberOfQuizzes(
                new EventCountDistinctVocablesWithTranslationsCompleted(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS)
        );

        verify(settings).setNumberOfQuestions(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS);
    }

    @Test
    public void test_onEventCalculateNumberOfQuizzes_IfNumberOfQuestionsAreLessThanPossibleQuestions_DontReduceThem() {
        int NUMBER_OF_QUESTIONS_FROM_SETTINGS = 1;
        int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 2;

        when(settings.getNumberOfQuestions()).thenReturn(NUMBER_OF_QUESTIONS_FROM_SETTINGS);

        model.onEventCalculateNumberOfQuizzes(
                new EventCountDistinctVocablesWithTranslationsCompleted(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS)
        );

        verify(settings, never()).setNumberOfQuestions(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS);
    }
}