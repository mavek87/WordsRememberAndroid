package com.matteoveroni.wordsremember.scene_quizgame.events;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Matteo Veroni
 */

@AllArgsConstructor
public class EventQuizUpdatedWithNewQuestion {

    @Getter
    private final Quiz quiz;
}
