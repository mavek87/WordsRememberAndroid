package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesToDoException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Veroni
 */

public class QuizGameModel {

    private int remainingNumberOfQuizzes;

    public QuizGameModel(int numberOfQuizzes) {
        if (numberOfQuizzes < 0)
            throw new IllegalArgumentException("Illegal argument passed");

        this.remainingNumberOfQuizzes = numberOfQuizzes;
    }

    public int getRemainingNumberOfQuizzes() {
        return remainingNumberOfQuizzes;
    }

    public Quiz getNextQuiz() throws NoMoreQuizzesToDoException {
        if (getRemainingNumberOfQuizzes() < 1) {
            throw new NoMoreQuizzesToDoException();
        }
        remainingNumberOfQuizzes--;

        List<String> rightAnswers = new ArrayList<>();
        rightAnswers.add("Good morning");
        rightAnswers.add("Good night");

        return new Quiz("What are the most commons english greetings?", rightAnswers);
    }
}
