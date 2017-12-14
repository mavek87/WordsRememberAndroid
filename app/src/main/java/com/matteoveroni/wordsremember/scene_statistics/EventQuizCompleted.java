package com.matteoveroni.wordsremember.scene_statistics;

import com.matteoveroni.wordsremember.scene_quizgame.pojos.Quiz;

/**
 * @author Matteo Veroni
 */

public class EventQuizCompleted {

    private final Quiz quiz;

    public EventQuizCompleted(Quiz quiz) {
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
