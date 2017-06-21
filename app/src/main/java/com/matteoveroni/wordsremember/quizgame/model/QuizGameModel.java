package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameModel {

    void startGame();

    void pauseGame();

    void abortGame();

    int getNumberOfQuestions();

    void generateQuiz() throws NoMoreQuizzesException, ZeroQuizzesException;

    void giveFinalAnswer(String finalAnswer);

    Quiz getCurrentQuiz();

    int getTotalScore();

    void setCurrentQuizFinalResult(Quiz.FinalResult result);
}
