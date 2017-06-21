package com.matteoveroni.wordsremember.quizgame.model;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Matteo Veroni
 */

public class QuizGameTimer extends CountDownTimer {

    // TODO: use variable stored in settings
    public static final int DEFAULT_TIME = 10000;
    public static final int DEFAULT_TICK = 1000;

    private final int tick;
    private final View androidViewShowingTimer;
    private final ArrayList<Listener> listeners = new ArrayList<>();

    public QuizGameTimer(int time, int tick, View androidViewShowingTimer) {
        super(time, tick);
        this.tick = tick;
        if (androidViewShowingTimer instanceof EditText || androidViewShowingTimer instanceof TextView) {
            this.androidViewShowingTimer = androidViewShowingTimer;
        } else {
            throw new IllegalArgumentException("androidViewShowingTimer is not a TextView or a EditText");
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (androidViewShowingTimer instanceof EditText) {
            ((EditText) (androidViewShowingTimer)).setText("seconds remaining: " + millisUntilFinished / tick);
        } else if (androidViewShowingTimer instanceof TextView) {
            ((TextView) (androidViewShowingTimer)).setText("seconds remaining: " + millisUntilFinished / tick);
        }
    }

    public void startToListen(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void onFinish() {
        for (Listener listener : listeners) {
            listener.onQuizGameTimerFinished();
        }
        listeners.clear();
    }

    public interface Listener {

        void onQuizGameTimerFinished();
    }
}
