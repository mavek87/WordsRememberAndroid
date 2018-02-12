package com.matteoveroni.wordsremember.scene_report;

import android.os.Bundle;
import android.widget.EditText;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportActivity extends AbstractPresentedActivityView implements QuizGameReportView {

    private QuizGameReportPresenter presenter;

    @BindView(R.id.quiz_game_report_txt_game_difficulty)
    EditText txt_gameDifficulty;

    @BindView(R.id.quiz_game_report_txt_total_number_of_questions)
    EditText txt_totNumberOfQuestions;

    @BindView(R.id.quiz_game_report_txt_number_of_correct_answers)
    EditText txt_numberOfCorrectAnswers;

    @BindView(R.id.quiz_game_report_txt_number_of_wrong_answers)
    EditText txt_numberOfWrongAnswers;

    @BindView(R.id.quiz_game_report_txt_correctness_percentage)
    EditText txt_correctnessPercentage;

    @BindView(R.id.quiz_game_report_txt_avg_response_time)
    EditText txt_avgResponseTime;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.QUIZ_GAME_REPORT_PRESENTER_FACTORY);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (QuizGameReportPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_game_report);
        ButterKnife.bind(this);

        setupAndShowToolbar("Report");
        hideAndroidKeyboard();
    }

    @Override
    public void showData(Quiz quiz) {
        GameDifficulty gameDifficulty = quiz.getGameDifficulty();
        txt_gameDifficulty.setText(gameDifficulty.toString());

        int totalNumberOfQuestions = quiz.getTotalNumberOfQuestions();
        txt_totNumberOfQuestions.setText(String.format("%d", totalNumberOfQuestions));

        int numberOfCorrectAnswers = quiz.getCorrectAnswers().size();
        txt_numberOfCorrectAnswers.setText(String.format("%d", numberOfCorrectAnswers));

        int numberOfWrongAnswers = quiz.getWrongAnswers().size();
        txt_numberOfWrongAnswers.setText(String.format("%d", numberOfWrongAnswers));

        double correctnessPercentage = 0;
        if (totalNumberOfQuestions > 0) {
            correctnessPercentage = ((double) numberOfCorrectAnswers / totalNumberOfQuestions) * 100;
            if (Double.isNaN(correctnessPercentage)) {
                correctnessPercentage = 0;
            }
        }
        txt_correctnessPercentage.setText(String.format("%.2f %%", correctnessPercentage));

        long averageResponseTime = quiz.getAverageResponseTime();
        txt_avgResponseTime.setText(String.format("%.1f sec.", ((double) averageResponseTime / 1000)));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.switchToView(Name.MAIN_MENU);
    }
}
