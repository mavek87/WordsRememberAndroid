package com.matteoveroni.wordsremember.scene_quizgame.events;

/**
 * @author Matteo Veroni
 */

public class EventQuizGameModelInitException {

    private final Exception exception;

    public EventQuizGameModelInitException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
