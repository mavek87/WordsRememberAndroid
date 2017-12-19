package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.ZeroQuizzesException;

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

    void generateQuestion() throws NoMoreQuizzesException, ZeroQuizzesException;

    Quiz getCurrentQuiz();

    void answerCurrentQuestion(String finalAnswer);

    int getFinalTotalScore() throws GameNotEndedYetException;

}
