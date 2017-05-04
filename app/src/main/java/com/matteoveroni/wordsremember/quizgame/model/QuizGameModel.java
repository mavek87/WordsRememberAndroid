package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameModel {

    void generateQuiz() throws NoMoreQuizzesException, ZeroQuizzesException;

    int getNumberOfQuestions();

    int getQuestionNumber();

    void increaseScore();

    int getScore();

    void registerToEventBus();

    void unregisterToEventBus();

    void reset();
}
