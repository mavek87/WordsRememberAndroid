package com.matteoveroni.wordsremember.quizgame.business_logic;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameTimer extends CountDownTimerPausable implements Serializable {

    // TODO: use variable stored in settings
    public static final long DEFAULT_TIME = 10000;
    public static final long DEFAULT_TICK = 1000;

    private final View androidViewShowingTimer;
    private final Set<Listener> listeners = new HashSet<>();

    public QuizGameTimer(long timeToCount, long countDownInterval, View androidViewShowingTimer) {
        super(timeToCount, countDownInterval);
        this.countDownInterval = countDownInterval;
        if (androidViewShowingTimer instanceof EditText || androidViewShowingTimer instanceof TextView) {
            this.androidViewShowingTimer = androidViewShowingTimer;
        } else {
            throw new IllegalArgumentException("androidViewShowingTimer is not a TextView or a EditText");
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (androidViewShowingTimer instanceof EditText) {
            ((EditText) (androidViewShowingTimer)).setText("Time remaining: " + millisUntilFinished / countDownInterval);
        } else if (androidViewShowingTimer instanceof TextView) {
            ((TextView) (androidViewShowingTimer)).setText("Time remaining: " + millisUntilFinished / countDownInterval);
        }
    }

    @Override
    public final void cancel() {
        super.cancel();
        listeners.clear();
    }

    @Override
    public void onFinish() {
        for (Listener listener : listeners) {
            listener.onQuizGameTimerFinished();
        }
        listeners.clear();
    }

    public void addTimerListener(Listener listener) {
        listeners.add(listener);
    }

    public interface Listener {

        void onQuizGameTimerFinished();
    }
}
