package com.matteoveroni.wordsremember.quizgame.business_logic;

/**
 * Created by Matteo Veroni
 */

public enum QuizGameDifficulty {

    EASY(1), MEDIUM(2), HARD(3);

    private int id;

    QuizGameDifficulty(int numberOfQuestions) {
        this.id = numberOfQuestions;
    }

    public int getId() {
        return id;
    }

    public final static int COMPLEXITY_MULTIPLIER = 10;
}
