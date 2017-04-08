package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Range;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.util.ExceptionToResourceMapping;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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

    private final Random randomGenerator = new SecureRandom();
    private final Set<Integer> randomlyExtractedIdsForQuizzes = new HashSet<>();

    private int numberOfIdsExtrated = 0;

    public QuizGameFindTranslationForVocableModel(GameDifficulty gameDifficulty, DictionaryDAO dao) {
        this.difficulty = gameDifficulty;
        this.dao = dao;

        registerToEventBus();

        setNumberOfQuizzes();
    }

    private void setNumberOfQuizzes() {
        dao.countUniqueVocablesWithTranslation();
    }

    @Subscribe
    public void onEvent(EventCountUniqueVocablesWithTranslationsCompleted event) {
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

        int maxNumberOfQuizzesCreatable = event.getNumberOfUniqueVocablesWithTranslation();
        if (numberOfQuizzes > maxNumberOfQuizzesCreatable) {
            numberOfQuizzes = maxNumberOfQuizzesCreatable;
        }
        Log.d(TAG, "Max number of quizzes creatable are: " + numberOfQuizzes);
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

    public void startQuizGeneration() throws NoMoreQuizzesException {
        try {
            int extractedUniqueRandomNumber = generateUniqueRandomInteger();
            dao.asyncSearchVocableWithTranslationByOffsetCommand(extractedUniqueRandomNumber);
        } catch (NoMoreUniqueRandomNumbersGenerable ex) {
            throw new NoMoreQuizzesException();
        }
    }

    private int generateUniqueRandomInteger() throws NoMoreUniqueRandomNumbersGenerable {
        int randomIntExtracted;
        int initialSetSize = randomlyExtractedIdsForQuizzes.size();

        Range range = new Range(1, numberOfQuizzes);

        numberOfIdsExtrated++;

        do {
            randomIntExtracted = randomInt(range);
            randomlyExtractedIdsForQuizzes.add(randomIntExtracted);
        } while (randomlyExtractedIdsForQuizzes.size() == initialSetSize);
        return randomIntExtracted;
    }

    private class NoMoreUniqueRandomNumbersGenerable extends Exception {
    }

    public int randomInt(Range range) {
        int min = range.getLowBorder();
        int max = range.getHighBorder();
        return randomGenerator.nextInt((max - min) + 1) + min;
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableWithTranslationByOffsetCompleted event) {
        Word vocable = event.getVocableWithTranslationFound();
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
