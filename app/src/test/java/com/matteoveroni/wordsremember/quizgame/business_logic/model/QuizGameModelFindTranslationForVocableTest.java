package com.matteoveroni.wordsremember.quizgame.business_logic.model;

import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelFindTranslationForVocableTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private Settings settings;
    @Mock
    private DictionaryDAO dao;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private static final Word FAKE_VOCABLE_EXTRACTED = new Word(1, "FakeVocable");

    private QuizGameModelFindTranslationForVocable model;

    @Before
    public void setUp() {
        model = new QuizGameModelFindTranslationForVocable(settings, dao);
    }

    @Test
    public void test_After_onEventCalculateNumberOfQuizzes_EventQuizModelInitialized() {
        final int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 1;

        model.onEventCountDistinctVocablesWithTranslations(
                new EventCountDistinctVocablesWithTranslationsCompleted(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS)
        );

        EVENT_BUS.hasSubscriberForEvent(EventQuizModelInitialized.class);
    }

    @Test
    public void test_onEventCalculateNumberOfQuizzes_IfNumberOfQuestionsAreMoreThanMaxNumberOfQuestions_ReduceThem() {
        final int MAX_NUMBER_OF_QUESTIONS_FROM_SETTINGS = 2;
        final int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 1;

        eventCalculateNumberOfQuestions(MAX_NUMBER_OF_QUESTIONS_FROM_SETTINGS, NUMBER_OF_VOCABLES_WITH_TRANSLATIONS);

        model.setNumberOfQuestions(MAX_NUMBER_OF_QUESTIONS_FROM_SETTINGS);
    }

    @Test
    public void test_onEventCalculateNumberOfQuizzes_IfNumberOfQuestionsAreLessThanMaxNumberOfQuestions_DontReduceThem() {
        final int MAX_NUMBER_OF_QUESTIONS_FROM_SETTINGS = 1;
        final int NUMBER_OF_VOCABLES_WITH_TRANSLATIONS = 2;

        eventCalculateNumberOfQuestions(MAX_NUMBER_OF_QUESTIONS_FROM_SETTINGS, NUMBER_OF_VOCABLES_WITH_TRANSLATIONS);

        model.setNumberOfQuestions(NUMBER_OF_VOCABLES_WITH_TRANSLATIONS);
    }

    private void eventCalculateNumberOfQuestions(int numberOfQuestionsFromSettings, int numberOfVocablesWithTranslations) {
        when(settings.getNumberOfQuestions()).thenReturn(numberOfQuestionsFromSettings);

        model.onEventCountDistinctVocablesWithTranslations(
                new EventCountDistinctVocablesWithTranslationsCompleted(numberOfVocablesWithTranslations)
        );
    }

    @Test(expected = ZeroQuizzesException.class)
    public void test_onStartQuizGeneration_If_getNumberOfQuizzesFromSettingsIsZero_throwZeroQuizzesException() throws NoMoreQuizzesException, ZeroQuizzesException {
        when(settings.getNumberOfQuestions()).thenReturn(0);

        model.generateQuiz();
    }

    @Test
    public void test_onStartQuizGeneration_If_getNumberOfQuizzesGreaterThanZero_extractRandomQuiz() throws NoMoreQuizzesException, ZeroQuizzesException {
        model.setNumberOfQuestions(1);
        when(settings.getNumberOfQuestions()).thenReturn(1);

        model.generateQuiz();

        verify(dao).asyncSearchDistinctVocableWithTranslationByOffset(anyInt());
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