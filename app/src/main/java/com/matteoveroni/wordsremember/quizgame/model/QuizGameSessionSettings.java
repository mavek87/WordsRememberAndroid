package com.matteoveroni.wordsremember.quizgame.model;

/**
 * Created by Matteo Veroni
 */

public class QuizGameSessionSettings {

    private QuizGameDifficulty difficulty;
    private int numberOfQuestions;

    public QuizGameSessionSettings() {
        this(QuizGameDifficulty.EASY);
    }

    public QuizGameSessionSettings(QuizGameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.numberOfQuestions = this.difficulty.getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public QuizGameDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuizGameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int number) {
        numberOfQuestions = number;
    }
}
