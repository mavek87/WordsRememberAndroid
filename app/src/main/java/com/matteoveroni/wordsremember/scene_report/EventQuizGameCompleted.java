package com.matteoveroni.wordsremember.scene_report;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

/**
 * @author Matteo Veroni
 */

public class EventQuizGameCompleted {
    private final Quiz quiz;

    public EventQuizGameCompleted(Quiz quiz) {
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
