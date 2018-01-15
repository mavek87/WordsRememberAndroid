package com.matteoveroni.wordsremember.scene_report;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BasePresentedActivityView;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenter;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportActivity extends BasePresentedActivityView implements QuizGameReportView {

    private QuizGameReportPresenter presenter;

    @BindView(R.id.quiz_game_report_text)
    TextView text;

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

//        setupAndShowToolbar(getString(R.string.add_translation));
        setupAndShowToolbar("Report");
    }

    @Override
    public void showData(Quiz quiz) {
        String dataToShow = "";

        GameDifficulty gameDifficulty = quiz.getGameDifficulty();
        int numberOfCorrectQuestions = quiz.getCorrectQuestions().size();
        int numberOfWrongQuestions = quiz.getWrongQuestions().size();
        int totalNumberOfQuestions = quiz.getTotalNumberOfQuestions();

        dataToShow = gameDifficulty.toString() + " " + totalNumberOfQuestions + " " + numberOfCorrectQuestions + " " + numberOfWrongQuestions;
        text.setText(dataToShow);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.switchToView(Name.MAIN_MENU);
    }
}
