package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.wordsremember.Settings;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

public class QuizGameFindTranslationForVocableModelTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private Settings settings;
    @Mock
    private DictionaryDAO dao;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private static final Word FAKE_VOCABLE_EXTRACTED = new Word(1, "FakeVocable");

    private QuizGameFindTranslationForVocableModel model;

    @Before
    public void setUp() {
        model = new QuizGameFindTranslationForVocableModel(settings, dao);
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

    @Test(expected = ZeroQuizzesException.class)
    public void test_onStartQuizGeneration_If_getNumberOfQuizzesFromSettingsIsZero_throwZeroQuizzesException() throws NoMoreQuizzesException, ZeroQuizzesException {
        when(settings.getNumberOfQuestions()).thenReturn(0);

        model.startQuizGeneration();
    }

    @Test
    public void test_onStartQuizGeneration_If_getNumberOfQuizzesGreaterThanZero_extractRandomQuiz() throws NoMoreQuizzesException, ZeroQuizzesException {
        model.numberOfQuestions = 1;
        when(settings.getNumberOfQuestions()).thenReturn(1);

        model.startQuizGeneration();

        verify(dao).asyncSearchVocableWithTranslationByOffsetCommand(anyInt());
    }

    @Test
    public void test_onEventGetExtractedVocableId_DAO_searchVocableById() {
        model.onEventGetExtractedVocableId(
                new EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted(FAKE_VOCABLE_EXTRACTED.getId())
        );

        verify(dao).asyncSearchVocableById(FAKE_VOCABLE_EXTRACTED.getId());
    }

    @Test
    public void test_onEventGetExtractedVocable_DAO_searchVocableTranslations() {
        model.onEventGetExtractedVocable(new EventAsyncSearchVocableCompleted(FAKE_VOCABLE_EXTRACTED));

        verify(dao).asyncSearchVocableTranslations(FAKE_VOCABLE_EXTRACTED);
    }

}