package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.NoMoreQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.ZeroQuestionsException;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameModel {

    public class GameNotEndedYetException extends Exception {
    }

    void startGame();

    void pauseGame();

    void stopGame();

    boolean isGameStopped();

    int getNumberOfQuestions();

    void generateQuestion() throws NoMoreQuestionsException, ZeroQuestionsException;

    Quiz getCurrentQuiz();

    void answerCurrentQuestion(String finalAnswer);

    int getFinalTotalScore() throws GameNotEndedYetException;

}
