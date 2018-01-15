package com.matteoveroni.wordsremember.scene_report;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportPresenter extends BasePresenter<QuizGameReportView> {
    public static final String TAG = TagGenerator.tag(QuizGameReportPresenter.class);

    @Override
    public void attachView(QuizGameReportView view) {
        super.attachView(view);

    }

    @Subscribe(sticky = true)
    public void onEventQuizGameCompleted(EventQuizGameCompleted event) {
        final Quiz quiz = event.getQuiz();
        view.showData(quiz);
    }

}
