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
            view.showData(getQuizViewAdapter(quiz));
        } catch (Throwable ex) {
            Log.e(TagGenerator.tag(QuizGamePresenter.class), ex.getMessage());
        }
    }

    private QuizViewAdapter getQuizViewAdapter(Quiz quiz) {
        String str_gameDifficulty = quiz.getGameDifficulty().toString();

        int totalNumberOfQuestions = quiz.getTotalNumberOfQuestions();
        String str_totalNumberOfQuestions = String.valueOf(totalNumberOfQuestions);

        int numberOfCorrectAnswers = quiz.getCorrectAnswers().size();
        String str_numberOfCorrectAnswers = String.valueOf(numberOfCorrectAnswers);

        int numberOfWrongAnswers = quiz.getWrongAnswers().size();
        String str_numberOfWrongAnswers = String.valueOf(numberOfWrongAnswers);

        double correctnessPercentage = 0;
        if (totalNumberOfQuestions > 0) {
            correctnessPercentage = ((double) numberOfCorrectAnswers / numberOfWrongAnswers * 100);
            if (Double.isNaN(correctnessPercentage)) {
                correctnessPercentage = 0;
            }
        }
        String str_correctnessPercentage = String.format("%.2f %%", correctnessPercentage);

        long averageResponseTime = quiz.getAverageResponseTime();
        String str_averageResponseTime = String.format("%.1f sec.", ((double) averageResponseTime / 1000));

        return new QuizViewAdapter(
                str_gameDifficulty,
                str_totalNumberOfQuestions,
                str_numberOfCorrectAnswers,
                str_numberOfWrongAnswers,
                str_correctnessPercentage,
                str_averageResponseTime
        );
    }
}
