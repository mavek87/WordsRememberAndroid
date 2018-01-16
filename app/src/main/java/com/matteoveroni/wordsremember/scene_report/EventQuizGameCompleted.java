package com.matteoveroni.wordsremember.scene_report;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class EventQuizGameCompleted {

    @Getter
    private final Quiz quiz;
}
