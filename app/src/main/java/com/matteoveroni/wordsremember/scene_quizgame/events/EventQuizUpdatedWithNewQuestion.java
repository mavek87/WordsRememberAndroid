package com.matteoveroni.wordsremember.scene_quizgame.events;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;

/**
 * Created by Matteo Veroni
 */

public class EventQuizUpdatedWithNewQuestion {

    private final Quiz quiz;

    public EventQuizUpdatedWithNewQuestion(Quiz quiz) {
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
