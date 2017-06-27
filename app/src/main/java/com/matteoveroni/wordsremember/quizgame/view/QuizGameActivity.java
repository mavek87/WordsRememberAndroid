package com.matteoveroni.wordsremember.quizgame.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizGameTimer;
import com.matteoveroni.wordsremember.quizgame.business_logic.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.business_logic.presenter.QuizGamePresenterFactory;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.view.dialogs.QuizResultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matteo Veroni
 */

public class QuizGameActivity extends BaseActivityPresentedView implements QuizGameView, QuizGameTimer.TimerPrinter, QuizResultDialog.QuizResultDialogListener {

    public static final String TAG = TagGenerator.tag(QuizGameActivity.class);

    public static final String QUIZ_GAME_TIMER_KEY = "quiz_game_timer";
    public static final String LBL_QUESTION_KEY = "lbl_question";
    public static final String LBL_QUESTION_VOCABLE_KEY = "lbl_question_vocable";
    public static final String TXT_ANSWER_KEY = "txt_answer";
    public static final String IS_DIALOG_SHOWN_KEY = "is_dialog_shown";

    private static final QuizGamePresenterFactory PRESENTER_FACTORY = new QuizGamePresenterFactory();

    @BindView(R.id.lbl_remainingTime)
    TextView lbl_remainingTime;

    @BindView(R.id.quiz_game_question)
    TextView lbl_question;

    @BindView(R.id.quiz_game_question_vocable)
    TextView lbl_question_vocable;

    @BindView(R.id.quiz_game_answer_edit_text)
    EditText txt_answer;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog quizAlert;
    private QuizGamePresenter presenter;
    private Quiz currentQuiz;
    private QuizGameTimer quizGameTimer;

    private boolean isDialogShown = false;

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

        if (savedInstanceState == null) {
            quizGameTimer = new QuizGameTimer(this, QuizGameTimer.DEFAULT_TIME, QuizGameTimer.DEFAULT_TICK);
        } else {
            restoreViewData(savedInstanceState);
        }

        setSoftkeyActionButtonToConfirmQuizAnswer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        saveViewData(instanceState);
        stopQuizTimerCount();
    }

    private void saveViewData(Bundle instanceState) {
        instanceState.putString(LBL_QUESTION_KEY, lbl_question.getText().toString());
        instanceState.putString(LBL_QUESTION_VOCABLE_KEY, lbl_question_vocable.getText().toString());
        instanceState.putString(TXT_ANSWER_KEY, txt_answer.getText().toString());
        instanceState.putBoolean(IS_DIALOG_SHOWN_KEY, isDialogShown);
        instanceState.putLong(QUIZ_GAME_TIMER_KEY, quizGameTimer.getRemainingTime());
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState.containsKey(LBL_QUESTION_KEY)) {
            lbl_question.setText(instanceState.getString(LBL_QUESTION_KEY));
        }
        if (instanceState.containsKey(LBL_QUESTION_VOCABLE_KEY)) {
            lbl_question_vocable.setText(instanceState.getString(LBL_QUESTION_VOCABLE_KEY));
        }
        if (instanceState.containsKey(TXT_ANSWER_KEY)) {
            txt_answer.setText(instanceState.getString(TXT_ANSWER_KEY));
        }
        if (instanceState.containsKey(IS_DIALOG_SHOWN_KEY)) {
            isDialogShown = instanceState.getBoolean(IS_DIALOG_SHOWN_KEY);
        }
        if (instanceState.containsKey(QUIZ_GAME_TIMER_KEY)) {
            long timeRemainingInMillis = instanceState.getLong(QUIZ_GAME_TIMER_KEY, QuizGameTimer.DEFAULT_TIME);
            long timeRemainingInSeconds = (int) (timeRemainingInMillis / 1000);
            printTime(timeRemainingInSeconds);

            if (timeRemainingInMillis > 0) {
                quizGameTimer = new QuizGameTimer(this, timeRemainingInMillis, QuizGameTimer.DEFAULT_TICK);
            } else {
                quizGameTimer = new QuizGameTimer(this, QuizGameTimer.DEFAULT_TIME, QuizGameTimer.DEFAULT_TICK);
            }

            if (!isDialogShown) {
                startQuizTimerCount();
            }
        }
    }

    @Override
    public void printTime(long timeRemaining) {
        int time = (int) timeRemaining;
        lbl_remainingTime.setText(getString(R.string.timeRemaining) + ": " + String.valueOf(time));
    }

    @Override
    public void startQuizTimerCount() {
        quizGameTimer.addTimerListener(this.presenter);
        quizGameTimer.start();
    }

    @Override
    public void stopQuizTimerCount() {
        if (quizGameTimer != null) {
            quizGameTimer.cancel();
        }
    }

    @Override
    public void resetQuizTimerCount() {
        stopQuizTimerCount();
        quizGameTimer = new QuizGameTimer(this, QuizGameTimer.DEFAULT_TIME, QuizGameTimer.DEFAULT_TICK);
    }

    @Override
    public Quiz getPojoUsed() {
        return currentQuiz;
    }

    @Override
    public void setPojoUsed(Quiz quiz) {
        currentQuiz = quiz;
        lbl_question.setText(getString(R.string.translate_vocable) + " " + quiz.getQuizNumber() + "/" + quiz.getTotalNumberOfQuizzes());
        lbl_question_vocable.setText(currentQuiz.getQuestion());
        showAllViewFields(true);
    }

    @Override
    public void onBackPressed() {
        quitGame();
    }

    private void quitGame() {
        stopQuizTimerCount();
        presenter.abortGame();
        presenter.detachView();
        finish();
    }

    @Override
    public void showQuizResultDialog(Quiz.FinalResult quizFinalResult, FormattedString message) {
        hideAndroidKeyboard();

        String quizResultMessage = localize(message) + "\n\n" + getString(R.string.msg_press_ok_to_continue);

        QuizResultDialog quizResultDialog = QuizResultDialog.newInstance(quizFinalResult, quizResultMessage);
        quizResultDialog.show(getSupportFragmentManager(), QuizResultDialog.TAG);
        isDialogShown = true;
    }

    @Override
    public void confirmQuizAnswerAction() {
        String givenAnswer = txt_answer.getText().toString();
        presenter.onQuizAnswerFromView(givenAnswer);
    }

    @Override
    public void confirmQuizResultDialogAction() {
        presenter.playNextQuiz();
        isDialogShown = false;
        showAndroidKeyboard();
    }

    @Override
    public void showGameResultDialog(FormattedString gameResultMessage) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.game_result))
                .setMessage(localize(gameResultMessage))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isDialogShown = false;
                        quitGame();
                    }
                })
                .setCancelable(false);
        quizAlert = alertDialogBuilder.create();
        quizAlert.show();
        isDialogShown = true;
    }

    @Override
    public void showErrorDialog(String msgErrorText) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.error))
                .setMessage(msgErrorText)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isDialogShown = false;
                        presenter.abortGame();
                    }
                })
                .setCancelable(false);
        quizAlert = alertDialogBuilder.create();
        quizAlert.show();
        isDialogShown = true;
    }

    private void setSoftkeyActionButtonToConfirmQuizAnswer() {
        txt_answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    confirmQuizAnswerAction();
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
}
