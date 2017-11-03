package com.matteoveroni.wordsremember.scene_quizgame.events;

import com.matteoveroni.wordsremember.scene_quizgame.pojos.Quiz;

/**
 * Created by Matteo Veroni
 */

public class EventQuizGenerated {

    private final Quiz quiz;

    public EventQuizGenerated(Quiz quiz) {
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
