package com.matteoveroni.wordsremember.scene_report;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.piechart)
    PieChart pieChart;

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

        drawPieChart(numberOfCorrectAnswers, numberOfWrongAnswers);

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

    private void drawPieChart(float numberOfCorrectAnswers, float numberOfWrongAnswers) {
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);

        List<PieEntry> graphYValues = new ArrayList<>();
        graphYValues.add(new PieEntry(numberOfCorrectAnswers, getString(R.string.correct_answers)));
        graphYValues.add(new PieEntry(numberOfWrongAnswers, getString(R.string.wrong_answers)));

        PieDataSet dataSet = new PieDataSet(graphYValues, "");

        final int RED_COLOR_CODE = ColorTemplate.rgb("FF0000");
        final int GREEN_COLOR_CODE = ColorTemplate.rgb("00CC00");
        dataSet.setColors(RED_COLOR_CODE, GREEN_COLOR_CODE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
    }
}
