package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

/**
 * @author Matteo Veroni
 */

public enum QuizGameDifficulty {

    EASY(1), MEDIUM(2), HARD(3);

    public final static int COMPLEXITY_MULTIPLIER = 10;

    private int id;

    QuizGameDifficulty(int numberOfQuestions) {
        this.id = numberOfQuestions;
    }

    public int getId() {
        return id;
    }

}
