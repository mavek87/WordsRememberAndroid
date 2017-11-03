package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizTimer extends CountDownTimerPausable {

    private TimerPrinter timerPrinter;

    private final Set<TimerListener> quizTimerListeners = new HashSet<>();

    public interface TimerPrinter {
        void printTime(long timeRemaining);
    }

    public interface TimerListener {
        void onQuizTimeElapsed();
    }

    public QuizTimer(TimerPrinter timerPrinter, long timeToCount, long countDownInterval) {
        super(timeToCount, countDownInterval);
        this.timerPrinter = timerPrinter;
        this.countDownInterval = countDownInterval;
    }

    public void setTimerPrinter(TimerPrinter timerPrinter) {
        this.timerPrinter = timerPrinter;
    }

    public void addTimerListener(TimerListener listener) {
        quizTimerListeners.add(listener);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (timerPrinter != null)
            timerPrinter.printTime(millisUntilFinished / countDownInterval);
    }

    @Override
    public final void cancel() {
        super.cancel();
        quizTimerListeners.clear();
    }

    @Override
    public void onFinish() {
        for (TimerListener listener : quizTimerListeners) {
            listener.onQuizTimeElapsed();
        }
        quizTimerListeners.clear();
    }
}
