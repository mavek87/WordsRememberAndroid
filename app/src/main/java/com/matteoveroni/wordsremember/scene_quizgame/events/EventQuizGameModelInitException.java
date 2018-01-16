package com.matteoveroni.wordsremember.scene_quizgame.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class EventQuizGameModelInitException {

    @Getter
    private final Exception exception;
}
