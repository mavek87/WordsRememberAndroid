package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

    private Quiz generateQuizForFindingVocables() throws NoMoreQuizzesException {
        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

    public void destroy() {
    }
}
