package com.matteoveroni.wordsremember.scene_statistics;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class StatisticsCalculator {

    private final static EventBus EVENT_BUS = EventBus.getDefault();

    @Subscribe
    public void onEventQuizCompleted(EventQuizCompleted event) {
        Quiz quiz = event.getQuiz();
    }


}
