package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameModel {

    public static final String TAG = TagGenerator.tag(QuizGameModel.class);
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final GameType gameType;
    private final GameDifficulty difficulty;
    private final DictionaryDAO dao;
    private int numberOfQuizzes;

    private final Random randomGenerator = new SecureRandom();
    private final Set<Integer> randomlyExtractedIdsForQuizzes = new HashSet<>();

    public QuizGameModel(GameType gameType, GameDifficulty gameDifficulty, DictionaryDAO dao) {
        this.gameType = gameType;
        this.difficulty = gameDifficulty;
        this.dao = dao;

        registerToEventBus();

        setNumberOfQuizzesAccordingToGameDifficulty();
        adjustNumberOfQuizzesAccordingToGameType();
    }

    private void setNumberOfQuizzesAccordingToGameDifficulty() {
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
    }

    private void adjustNumberOfQuizzesAccordingToGameType() {
        switch (gameType) {
            case FIND_RIGHT_TRANSLATION_FOR_VOCABLES:
                dao.countUniqueVocablesWithTranslation();
                break;
            case FIND_RIGHT_VOCABLE_FOR_TRANSLATIONS:
                dao.countUniqueVocablesWithTranslation();
                // calculate if max number of quizzes is ok or change it
                break;
        }
    }

    @Subscribe
    public void onEvent(EventCountUniqueVocablesWithTranslationsCompleted event) {
        int numberOfQuizzesCreatable = event.getNumberOfUniqueVocablesWithTranslation();
        if (numberOfQuizzes > numberOfQuizzesCreatable) {
            numberOfQuizzes = numberOfQuizzesCreatable;
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

    public Quiz generateQuiz() throws NoMoreQuizzesException {
        switch (gameType) {
            case FIND_RIGHT_TRANSLATION_FOR_VOCABLES:
                return generateQuizForFindingTranslations();
            case FIND_RIGHT_VOCABLE_FOR_TRANSLATIONS:
                return generateQuizForFindingVocables();
            default:
                throw new RuntimeException("QuizGenerator.generateQuiz() runtime Error");
        }
    }

    private Quiz generateQuizForFindingTranslations() throws NoMoreQuizzesException {
        int extractedUniqueRandomNumber = extractUniqueRandomNumber();
        dao.asyncSearchVocableWithTranslationByOffsetCommand(extractedUniqueRandomNumber);


        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableWithTranslationByOffsetCompleted event) {
        Word vocable = event.getVocableWithTranslationFound();
        dao.asyncSearchVocableTranslationsByVocableId(vocable.getId());
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableTranslationsCompleted event) {
        List<Word> translations = event.getTranslations();
    }

    private Quiz generateQuizForFindingVocables() throws NoMoreQuizzesException {
        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

    private int extractUniqueRandomNumber() {
        int randomIntExtracted;
        int initialSetSize = randomlyExtractedIdsForQuizzes.size();
        do {
            randomIntExtracted = randomInt(1, numberOfQuizzes);
            randomlyExtractedIdsForQuizzes.add(randomIntExtracted);
        } while (randomlyExtractedIdsForQuizzes.size() == initialSetSize);
        return randomIntExtracted;
    }

    public int randomInt(int min, int max) {
        return randomGenerator.nextInt((max - min) + 1) + min;
    }
}
