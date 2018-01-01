package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game;

/**
 * @author Matteo Veroni
 */

public class GameRunningState {

    private boolean isStarted = false;
    private boolean isStopped = false;
    private boolean isPaused = false;

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted() {
        isStarted = true;
        isStopped = false;
        isPaused = false;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped() {
        isStarted = false;
        isStopped = true;
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused() {
        isStarted = false;
        isStopped = false;
        isPaused = true;
    }

    public void reset(){
        isStarted = false;
        isStopped = false;
        isPaused = false;
    }
}
