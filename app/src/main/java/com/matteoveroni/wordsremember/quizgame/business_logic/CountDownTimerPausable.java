package com.matteoveroni.wordsremember.quizgame.business_logic;

import android.os.CountDownTimer;

/**
 * This class uses the native CountDownTimer to
 * create a timer which could be paused and then
 * started again from the previous point. You can
 * provide implementation for onTick() and onFinish()
 * then use it in your projects.
 */
public abstract class CountDownTimerPausable {
    long timeToCount = 0;
    long countDownInterval = 0;
    long millisRemaining = 0;

    CountDownTimer countDownTimer = null;

    boolean isPaused = true;
    boolean isCanceled = false;

    public CountDownTimerPausable(long timeToCount, long countDownInterval) {
        super();
        this.timeToCount = timeToCount;
        this.countDownInterval = countDownInterval;
        this.millisRemaining = timeToCount;
    }

    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();

    /**
     * Cancel the countdown.
     */
    public void cancel() {
        if (!isCanceled) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            isCanceled = true;
        }
//        this.millisRemaining = 0;
    }

    /**
     * Start or Resume the countdown.
     *
     * @return CountDownTimerPausable current instance
     */
    public synchronized final CountDownTimerPausable start() {
        if (isCanceled) {
            throw new IllegalStateException("CountDownTimerPausable is canceled. Cannot pause it.");
        }
        if (isPaused) {
            createCountDownTimer();
            countDownTimer.start();
            isPaused = false;
        }
        return this;
    }

    private void createCountDownTimer() {
        countDownTimer = new CountDownTimer(millisRemaining, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;
                CountDownTimerPausable.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                CountDownTimerPausable.this.onFinish();
            }
        };
    }

    /**
     * Pauses the CountDownTimerPausable, so it could be resumed(start)
     * later from the same point where it was paused.
     */
    public void pause() throws IllegalStateException {
        if (isCanceled) {
            throw new IllegalStateException("CountDownTimerPausable is canceled. Cannot pause it.");
        }
        if (isPaused) {
            throw new IllegalStateException("CountDownTimerPausable is already in pause state, start counter before pausing it.");
        } else {
            countDownTimer.cancel();
        }
        isPaused = true;
    }

    public long getRemainingTimeInMillis() {
        return millisRemaining;
    }

    public int getRemainingTimeInSeconds() {
        return (int) (millisRemaining / countDownInterval);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}