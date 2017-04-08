package com.matteoveroni.wordsremember.quizgame.events;

import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

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
