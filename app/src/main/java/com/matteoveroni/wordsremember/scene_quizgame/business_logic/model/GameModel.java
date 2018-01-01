package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionCompleted;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.NoMoreQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.ZeroQuestionsException;

/**
 * Created by Matteo Veroni
 */

public interface GameModel {

    void start();

    void pause();

    void stop();

    int getNumberOfQuestions();

    void generateQuestion() throws NoMoreQuestionsException, ZeroQuestionsException;

    Quiz getCurrentQuiz();

    QuestionCompleted answerCurrentQuestion(String finalAnswer);

    int getFinalTotalScore();
}
