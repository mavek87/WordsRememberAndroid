package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Veroni
 */

public class QuizGameModel {

    private int numberOfQuizzes;
    private final QuizGenerator quizGenerator;

    public QuizGameModel(GameDifficulty difficulty, QuizGenerator quizGenerator) {
        this.quizGenerator = quizGenerator;
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

    public Quiz getNextQuiz() throws NoMoreQuizzesException {
        return quizGenerator.generateQuiz();
    }
}
