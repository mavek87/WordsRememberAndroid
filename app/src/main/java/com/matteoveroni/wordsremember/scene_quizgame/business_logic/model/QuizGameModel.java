package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

import com.matteoveroni.wordsremember.scene_quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.pojos.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameModel {

    public class GameNotEndedYetException extends Exception {
    }

    void startGame();

    void pauseGame();

    void abortGame();

    boolean isGameEnded();

    int getNumberOfQuestions();

    void generateQuiz() throws NoMoreQuizzesException, ZeroQuizzesException;

    Quiz getCurrentQuiz();

    void giveFinalAnswer(String finalAnswer);

    int getFinalTotalScore() throws GameNotEndedYetException;

    void setCurrentQuizFinalResult(Quiz.FinalResult result);
}
