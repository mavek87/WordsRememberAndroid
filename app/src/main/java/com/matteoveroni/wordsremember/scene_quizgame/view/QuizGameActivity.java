package com.matteoveroni.wordsremember.scene_quizgame.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuizTimer;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter.QuizGamePresenterFactory;
import com.matteoveroni.wordsremember.scene_quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.ErrorDialog;
import com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.GameResultDialog;
import com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.QuizResultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.ErrorDialog.ErrorDialogListener;
import static com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.GameResultDialog.GameResultDialogListener;
import static com.matteoveroni.wordsremember.scene_quizgame.view.dialogs.QuizResultDialog.QuizResultDialogListener;

/**
 * @author Matteo Veroni
 */

public class QuizGameActivity extends BaseActivityPresentedView implements
        QuizGameView, QuizTimer.TimerPrinter, QuizResultDialogListener,
        GameResultDialogListener, ErrorDialogListener {

    public static final String TAG = TagGenerator.tag(QuizGameActivity.class);

    public static final String CURRENT_QUIZ_KEY = "current_quiz_key";
    public static final String LBL_QUESTION_KEY = "lbl_question";
    public static final String LBL_QUESTION_VOCABLE_KEY = "lbl_question_vocable";
    public static final String TXT_ANSWER_KEY = "txt_answer";
    public static final String INT_PROGRESS_MAX_KEY = "int_progress_max_key";
    public static final String INT_PROGRESS_VALUE_KEY = "int_progress_value_key";

    private static final QuizGamePresenterFactory PRESENTER_FACTORY = new QuizGamePresenterFactory();

    @BindView(R.id.lbl_remainingTime)
    TextView lbl_remainingTime;

    @BindView(R.id.quiz_game_question)
    TextView lbl_question;

    @BindView(R.id.quiz_game_question_vocable)
    TextView lbl_question_vocable;

    @BindView(R.id.quiz_game_answer_edit_text)
    EditText txt_answer;

    @BindView(R.id.quiz_game_progress_bar)
    ProgressBar progressBar;

    private QuizGamePresenter presenter;
    private Quiz currentQuiz;
    private FragmentManager fragmentManager;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PRESENTER_FACTORY;
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (QuizGamePresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.quiz_game));
        fragmentManager = getSupportFragmentManager();
        progressBar.setProgress(0);

        if (savedInstanceState != null) {
            restoreViewData(savedInstanceState);
        }

        setSoftkeyActionButtonToConfirmQuizAnswer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        saveViewData(instanceState);
    }

    private void saveViewData(Bundle instanceState) {
        instanceState.putString(CURRENT_QUIZ_KEY, Json.getInstance().toJson(currentQuiz));
        instanceState.putString(LBL_QUESTION_KEY, lbl_question.getText().toString());
        instanceState.putString(LBL_QUESTION_VOCABLE_KEY, lbl_question_vocable.getText().toString());
        instanceState.putString(TXT_ANSWER_KEY, txt_answer.getText().toString());
        instanceState.putInt(INT_PROGRESS_MAX_KEY, progressBar.getMax());
        instanceState.putInt(INT_PROGRESS_VALUE_KEY, progressBar.getProgress());
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState.containsKey(CURRENT_QUIZ_KEY)) {
            String json_currentQuiz = instanceState.getString(CURRENT_QUIZ_KEY, null);
            if (json_currentQuiz == null)
                throw new RuntimeException("Saved invalid \'current quiz\' after device rotation. Impossible to restore old model.");

            currentQuiz = Json.getInstance().fromJson(json_currentQuiz, Quiz.class);
        }
        if (instanceState.containsKey(LBL_QUESTION_KEY)) {
            lbl_question.setText(instanceState.getString(LBL_QUESTION_KEY));
        }
        if (instanceState.containsKey(LBL_QUESTION_VOCABLE_KEY)) {
            lbl_question_vocable.setText(instanceState.getString(LBL_QUESTION_VOCABLE_KEY));
        }
        if (instanceState.containsKey(TXT_ANSWER_KEY)) {
            txt_answer.setText(instanceState.getString(TXT_ANSWER_KEY));
        }
        if (instanceState.containsKey(INT_PROGRESS_MAX_KEY)) {
            progressBar.setMax(instanceState.getInt(INT_PROGRESS_MAX_KEY));
        }
        if (instanceState.containsKey(INT_PROGRESS_VALUE_KEY)) {
            progressBar.setProgress(instanceState.getInt(INT_PROGRESS_VALUE_KEY));
        }

        printTime(presenter.getRemainingTimeForCurrentQuizInSeconds());
    }

    @Override
    public Quiz getPojoUsed() {
        return currentQuiz;
    }

    @Override
    public void setPojoUsed(Quiz quiz) {
        int totNumQuizQuestions = quiz.getTotalNumberOfQuestions();
        int quizQuestionNumber = quiz.getQuizQuestionNumber();

        currentQuiz = quiz;
        lbl_question.setText(String.format("%s %d/%d", getString(R.string.translate_vocable), quizQuestionNumber, totNumQuizQuestions));

        progressBar.setMax(currentQuiz.getTotalNumberOfQuestions());

        lbl_question_vocable.setText(currentQuiz.getQuestion());
        showAllViewFields(true);
    }

    @Override
    public void printTime(long timeRemaining) {
        int time = (int) timeRemaining;
        lbl_remainingTime.setText(getString(R.string.timeRemaining) + ": " + String.valueOf(time));
    }

    @Override
    public void showKeyboard() {
        showAndroidKeyboard();
    }

    @Override
    public void hideKeyboard() {
        hideAndroidKeyboard();
    }

    @Override
    public void onBackPressed() {
        quitGame();
    }

    @Override
    public void quitGame() {
        finish();
    }

    @Override
    public void giveQuizAnswerAction() {
        String givenAnswer = txt_answer.getText().toString();
        presenter.onQuizAnswerFromView(givenAnswer);
    }

    @Override
    public void showQuizResultDialog(Quiz.FinalResult quizFinalResult, FormattedString message) {
//        updateQuizProgressBar();
        progressBar.setProgress(currentQuiz.getQuizQuestionNumber());

        hideAndroidKeyboard();

        String quizResultMessage = localize(message) + "\n\n" + getString(R.string.msg_press_ok_to_continue);

        QuizResultDialog quizResultDialog = QuizResultDialog.newInstance(quizFinalResult, quizResultMessage);
        quizResultDialog.show(fragmentManager, QuizResultDialog.TAG);
    }

    private void updateQuizProgressBar() {
        progressBar.setMax(currentQuiz.getTotalNumberOfQuestions());
        progressBar.setProgress(currentQuiz.getQuizQuestionNumber());
    }

    @Override
    public void confirmQuizResultDialogAction() {
        presenter.onQuizResultDialogConfirmation();
    }

    @Override
    public void showGameResultDialog(FormattedString gameResultMessage) {
        hideAndroidKeyboard();

        String title = getString(R.string.game_result);
        String message = localize(gameResultMessage);

        GameResultDialog dialog = GameResultDialog.newInstance(title, message);
        dialog.show(fragmentManager, GameResultDialog.TAG);
    }

    @Override
    public void confirmGameResultDialogAction() {
        presenter.onGameResultDialogConfirmation();
    }

    @Override
    public void showErrorDialog(String msgError) {
        hideAndroidKeyboard();

        String title = getString(R.string.game_result);
        String message = localize(msgError);

        ErrorDialog dialog = ErrorDialog.newInstance(title, message);
        dialog.show(fragmentManager, ErrorDialog.TAG);
    }

    @Override
    public void confirmErrorDialogAction() {
        presenter.onErrorDialogConfirmation();
    }

    private void setSoftkeyActionButtonToConfirmQuizAnswer() {
        txt_answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    giveQuizAnswerAction();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void clearAndHideFields() {
        lbl_remainingTime.setText("");
        lbl_question.setText("");
        txt_answer.setText("");
        showAllViewFields(false);
    }

    private void showAllViewFields(boolean isViewVisible) {
        int visibility = isViewVisible ? View.VISIBLE : View.INVISIBLE;
        lbl_remainingTime.setVisibility(visibility);
        lbl_question.setVisibility(visibility);
        lbl_question_vocable.setVisibility(visibility);
        txt_answer.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        hideAndroidKeyboard();
        super.onDestroy();
    }
}
