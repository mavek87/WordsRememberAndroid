package com.matteoveroni.wordsremember.scene_statistics;

import com.matteoveroni.wordsremember.persistency.dao.StatisticsDAO;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class StatisticsCalculator {

    private final static EventBus EVENT_BUS = EventBus.getDefault();

    @Inject
    StatisticsDAO dao;

    @Subscribe
    public void onEventQuizCompleted(EventQuizCompleted event) {
        Quiz quiz = event.getQuiz();
        dao.saveQuizResults(quiz);


    }


}
