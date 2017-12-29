package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

/**
 * @author Matteo Veroni
 */

public enum GameDifficulty {

    EASY(1), MEDIUM(2), HARD(3);

    public final static int COMPLEXITY_MULTIPLIER = 10;

    private int id;

    GameDifficulty(int numberOfQuestions) {
        this.id = numberOfQuestions;
    }

    public int getId() {
        return id;
    }

}
