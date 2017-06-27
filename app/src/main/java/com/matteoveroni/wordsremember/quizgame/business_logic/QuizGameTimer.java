package com.matteoveroni.wordsremember.quizgame.business_logic;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameTimer extends CountDownTimerPausable {

    // TODO: use variable stored in settings
    public static final long DEFAULT_TIME = 10000;
    public static final long DEFAULT_TICK = 1000;

    public interface TimerPrinter {
        void printTime(long timeRemaining);
    }

    private final TimerPrinter timerPrinter;

    public interface TimerListener {
        void onQuizGameTimerFinished();
    }

    private final Set<TimerListener> timerListeners = new HashSet<>();

    public QuizGameTimer(TimerPrinter timerPrinter, long timeToCount, long countDownInterval) {
        super(timeToCount, countDownInterval);
        this.timerPrinter = timerPrinter;
        this.countDownInterval = countDownInterval;
    }

    public void addTimerListener(TimerListener listener) {
        timerListeners.add(listener);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerPrinter.printTime(millisUntilFinished / countDownInterval);
    }

    @Override
    public final void cancel() {
        super.cancel();
        timerListeners.clear();
    }

    @Override
    public void onFinish() {
        for (TimerListener listener : timerListeners) {
            listener.onQuizGameTimerFinished();
        }
        timerListeners.clear();
    }
}
