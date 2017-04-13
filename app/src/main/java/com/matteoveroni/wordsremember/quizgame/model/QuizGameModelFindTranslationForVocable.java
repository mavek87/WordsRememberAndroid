package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelFindTranslationForVocable {

    public static final String TAG = TagGenerator.tag(QuizGameModelFindTranslationForVocable.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final QuizGameSessionSettings settings;

    private final Set<Integer> randomlyExtractedPositionsForQuiz = new HashSet<>();
    private int numberOfVocablesWithTranslations = 0;

    public QuizGameModelFindTranslationForVocable(QuizGameSessionSettings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;

        registerToEventBus();

        adjustNumberOfQuizzesCountingMaxNumberOfQuizCreatable();
    }

    public void reset() {
        randomlyExtractedPositionsForQuiz.clear();
        numberOfVocablesWithTranslations = 0;
    }

    public void registerToEventBus() {
        if (!EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.register(this);
        }
    }

    public void unregisterToEventBus() {
        if (EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.unregister(this);
        }
    }

    private void adjustNumberOfQuizzesCountingMaxNumberOfQuizCreatable() {
        dao.countDistinctVocablesWithTranslations();
    }

    @Subscribe
    public void onEventCalculateNumberOfQuizzes(EventCountDistinctVocablesWithTranslationsCompleted event) {
        numberOfVocablesWithTranslations = event.getNumberOfVocablesWithTranslation();

        if (settings.getNumberOfQuestions() > numberOfVocablesWithTranslations) {
            settings.setNumberOfQuestions(numberOfVocablesWithTranslations);
        }

        Log.d(TAG, "Max number of quizzes creatable are: " + settings.getNumberOfQuestions());
        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    public void startQuizGeneration() throws NoMoreQuizzesException, ZeroQuizzesException {
        if (settings.getNumberOfQuestions() <= 0) throw new ZeroQuizzesException();
        try {
            int vocablePosition = extractUniqueRandomVocablePosition();
            dao.asyncSearchVocableWithTranslationByOffsetCommand(vocablePosition);
        } catch (NoMoreQuizzesException ex) {
            throw new NoMoreQuizzesException();
        }
    }

    private int extractUniqueRandomVocablePosition() throws NoMoreQuizzesException {
        int initialNumberOfPositionsExtractedForQuiz = randomlyExtractedPositionsForQuiz.size();

        IntRange positionsRange = new IntRange(0, numberOfVocablesWithTranslations - 1);
        if (initialNumberOfPositionsExtractedForQuiz >= settings.getNumberOfQuestions()) {
            throw new NoMoreQuizzesException();
        }

        int randPosition;

        do {
            randPosition = Int.getRandomInteger(positionsRange);
            randomlyExtractedPositionsForQuiz.add(randPosition);
        }
        while (randomlyExtractedPositionsForQuiz.size() == initialNumberOfPositionsExtractedForQuiz);

        return randPosition;
    }

    @Subscribe
    public void onEvent(EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted event) {
        long vocableId = event.getVocableWithTranslationFound();
        dao.asyncSearchVocableById(vocableId);
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableCompleted event) {
        Word vocable = event.getVocable();
        dao.asyncSearchVocableTranslations(vocable);
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableTranslationsCompleted event) {
        Word vocable = event.getVocable();
        String question = "What is the translation for the word: " + vocable.getName();

        List<Word> translations = event.getTranslations();
        List<String> answers = populateRightAnswers(translations);

        EVENT_BUS.post(new EventQuizGenerated(new Quiz(question, answers)));
    }

    private List<String> populateRightAnswers(List<Word> translations) {
        List<String> answers = new ArrayList<>();
        for (Word translation : translations) {
            answers.add(translation.getName());
        }
        return answers;
    }
}
