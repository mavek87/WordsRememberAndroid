package com.matteoveroni.wordsremember.quizgame.business_logic;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.wordsremember.quizgame.view.QuizGameActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameTimer extends CountDownTimerPausable {

    // TODO: use variable stored in settings
    public static final long DEFAULT_TIME_IN_MILLIS = 10000;
    public static final int DEFAULT_TIME_IN_SECONDS = (int) (DEFAULT_TIME_IN_MILLIS / 1000);
    public static final long DEFAULT_TICK = 1000;

    private final Set<Listener> listeners = new HashSet<>();

    private QuizGameActivity quizGameActivity;

    public QuizGameTimer(QuizGameActivity activity, long timeToCount, long countDownInterval) {
        super(timeToCount, countDownInterval);
        this.quizGameActivity = activity;
        this.countDownInterval = countDownInterval;
    }

    public long getCountDownInterval() {
        return countDownInterval;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        quizGameActivity.printTime(millisUntilFinished / countDownInterval);
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

    public void destroy() {
        quizGameActivity = null;
    }
}
