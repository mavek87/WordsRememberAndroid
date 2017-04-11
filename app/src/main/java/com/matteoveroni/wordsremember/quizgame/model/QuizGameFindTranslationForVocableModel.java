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
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreUniqueRandomVocableGenerable;
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

public class QuizGameFindTranslationForVocableModel {

    public static final String TAG = TagGenerator.tag(QuizGameFindTranslationForVocableModel.class);
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final QuizGameSessionSettings settings;
    private final DictionaryDAO dao;
    private int numberOfQuizzes;

    private Set<Integer> randomlyExtractedPositionsForQuiz = new HashSet<>();

    private int numberOfPositionsExtrated = 0;

    public QuizGameFindTranslationForVocableModel(QuizGameSessionSettings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;

        registerToEventBus();

        adjustNumberOfQuizzesCountingMaxNumberOfQuizCreatable();
    }

    public void reset() {
        randomlyExtractedPositionsForQuiz.clear();
        numberOfPositionsExtrated = 0;
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
    public void onEvent(EventCountDistinctVocablesWithTranslationsCompleted event) {
        int maxNumberOfQuizzesCreatable = event.getNumberOfUniqueVocablesWithTranslation();
        if (settings.getNumberOfQuestions() > maxNumberOfQuizzesCreatable) {
            settings.setNumberOfQuestions(maxNumberOfQuizzesCreatable);
        }
        Log.d(TAG, "Max number of quizzes creatable are: " + settings.getNumberOfQuestions() );
        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    public void startQuizGeneration() throws NoMoreQuizzesException, ZeroQuizzesException {
        if (numberOfQuizzes <= 0) throw new ZeroQuizzesException();
        try {
            int position = generateUniqueRandomVocablePosition();
            dao.asyncSearchVocableWithTranslationByOffsetCommand(position);
        } catch (NoMoreUniqueRandomVocableGenerable ex) {
            throw new NoMoreQuizzesException();
        }
    }

    private int generateUniqueRandomVocablePosition() throws NoMoreUniqueRandomVocableGenerable {
        IntRange positionsRange = new IntRange(0, settings.getNumberOfQuestions() - 1);

        if ((positionsRange.getDimension() - numberOfPositionsExtrated) < 0) {
            throw new NoMoreUniqueRandomVocableGenerable();
        }

        int randPosition;
        int initialSetSize = randomlyExtractedPositionsForQuiz.size();

        do {
            randPosition = Int.getRandomInteger(positionsRange);
            randomlyExtractedPositionsForQuiz.add(randPosition);
        } while (randomlyExtractedPositionsForQuiz.size() == initialSetSize);

        numberOfPositionsExtrated++;

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
