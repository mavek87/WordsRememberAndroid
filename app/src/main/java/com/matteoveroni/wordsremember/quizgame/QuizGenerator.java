package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class QuizGenerator {

    private final GameType type;
    private final DictionaryDAO dao;

    public QuizGenerator(GameType gameType, DictionaryDAO dao) {
        this.type = gameType;
        this.dao = dao;
    }

    public Quiz generateQuiz() throws NoMoreQuizzesException {
        switch (type) {
            case FIND_TRANSLATIONS:
                return generateQuizForFindingTranslations();
            case FIND_VOCABLES:
                return generateQuizForFindingVocables();
            default:
                throw new RuntimeException("QuizGenerator.generateQuiz() runtime Error");
        }
    }

    private Quiz generateQuizForFindingTranslations() throws NoMoreQuizzesException {
        dao.asyncGetNumberOfTranslationsForVocables();
        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

    private Quiz generateQuizForFindingVocables() throws NoMoreQuizzesException {
        dao.asyncGetNumberOfTranslationsForVocables();
        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }

}
