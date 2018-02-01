package com.matteoveroni.wordsremember.scene_report;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.persistency.dao.StatisticsDAO;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenter;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportPresenter extends BasePresenter<QuizGameReportView> {

    private final StatisticsDAO statisticsDAO;

    public QuizGameReportPresenter(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    @Override
    public void attachView(QuizGameReportView view) {
        super.attachView(view);
    }

    @Subscribe(sticky = true)
    public void onEvent(EventQuizGameCompleted event) {
        try {
            Quiz quiz = event.getQuiz();
            statisticsDAO.saveQuizResults(quiz);
            view.showData(quiz);
        }catch (Throwable ex) {
            Log.e(TagGenerator.tag(QuizGamePresenter.class), ex.getMessage());
        }
    }
}
