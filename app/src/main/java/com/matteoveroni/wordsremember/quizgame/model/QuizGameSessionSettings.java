package com.matteoveroni.wordsremember.quizgame.model;

/**
 * Created by Matteo Veroni
 */

public class QuizGameSessionSettings {

    private QuizGameDifficulty difficulty;
    private int numberOfQuestions;

    public QuizGameSessionSettings() {
    }

    public QuizGameSessionSettings(QuizGameDifficulty difficulty) {
        setDifficulty(difficulty);
    }

    public QuizGameDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuizGameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.numberOfQuestions = this.difficulty.getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Number of questions cannot be negative");

        numberOfQuestions = number;
    }
}
