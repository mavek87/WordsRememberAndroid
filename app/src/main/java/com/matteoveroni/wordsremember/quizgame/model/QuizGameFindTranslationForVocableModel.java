package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreUniqueRandomIntegerGenerable;
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

    private final GameDifficulty difficulty;
    private final DictionaryDAO dao;
    private int numberOfQuizzes;

    private Set<Integer> randomlyExtractedPositionsForQuiz = new HashSet<>();

    private int numberOfPositionsExtrated = 0;

    public QuizGameFindTranslationForVocableModel(GameDifficulty gameDifficulty, DictionaryDAO dao) {
        this.difficulty = gameDifficulty;
        this.dao = dao;

        registerToEventBus();

        switch (difficulty) {
            case EASY:
                numberOfQuizzes = 10;
                break;
            case MEDIUM:
                numberOfQuizzes = 20;
                break;
            case HARD:
                numberOfQuizzes = 30;
                break;
        }

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
        dao.countUniqueVocablesWithTranslation();
    }

    @Subscribe
    public void onEvent(EventCountUniqueVocablesWithTranslationsCompleted event) {
        int maxNumberOfQuizzesCreatable = event.getNumberOfUniqueVocablesWithTranslation();
        if (numberOfQuizzes > maxNumberOfQuizzesCreatable) {
            numberOfQuizzes = maxNumberOfQuizzesCreatable;
        }
        Log.d(TAG, "Max number of quizzes creatable are: " + numberOfQuizzes);

        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    public void startQuizGeneration() throws NoMoreQuizzesException {
        try {
            int uniqueRandomVocablePosition = generateUniqueRandomVocablePosition();
            dao.asyncSearchVocableWithTranslationByOffsetCommand(uniqueRandomVocablePosition);
        } catch (NoMoreUniqueRandomIntegerGenerable ex) {
            throw new NoMoreQuizzesException();
        }
    }

    private int generateUniqueRandomVocablePosition() throws NoMoreUniqueRandomIntegerGenerable {
        IntRange positionsRange = new IntRange(0, numberOfQuizzes - 1);

        if ((positionsRange.getDimension() - numberOfPositionsExtrated) <= 0) {
            throw new NoMoreUniqueRandomIntegerGenerable();
        }

        int randomPosition;
        int initialSetSize = randomlyExtractedPositionsForQuiz.size();

        do {
            randomPosition = Int.getRandomInteger(positionsRange);
            randomlyExtractedPositionsForQuiz.add(randomPosition);
        } while (randomlyExtractedPositionsForQuiz.size() == initialSetSize);

        numberOfPositionsExtrated++;

        return randomPosition;
    }


    @Subscribe
    public void onEvent(EventAsyncSearchVocableWithTranslationByOffsetCompleted event) {
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
        List<String> answers = new ArrayList<>();

        for (Word translation : translations) {
            answers.add(translation.getName());
        }

        EVENT_BUS.post(new EventQuizGenerated(new Quiz(question, answers)));
    }
}
