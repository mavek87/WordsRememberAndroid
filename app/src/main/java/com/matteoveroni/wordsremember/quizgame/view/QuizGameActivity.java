package com.matteoveroni.wordsremember.quizgame.view;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameTimer;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenterFactory;

import java.util.function.ToDoubleBiFunction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matteo Veroni
 */

public class QuizGameActivity extends BaseActivityPresentedView implements QuizGameView {

    @BindView(R.id.lbl_remainingTime)
    TextView lbl_remainingTime;

    @BindView(R.id.quiz_game_question)
    TextView lbl_question;

    @BindView(R.id.quiz_game_question_vocable)
    TextView lbl_question_vocable;

    @BindView(R.id.quiz_game_answer_edit_text)
    EditText txt_answer;

    private Quiz currentQuiz;
    private AlertDialog quizAlert;
    private AlertDialog.Builder alertDialogBuilder;
    private QuizGamePresenter presenter;

    public static final String LBL_QUESTION = "lbl_question";
    public static final String LBL_QUESTION_VOCABLE = "lbl_question_vocable";
    public static final String TXT_ANSWER = "txt_answer";

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new QuizGamePresenterFactory();
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

        if (savedInstanceState != null) restoreViewData(savedInstanceState);

        setSoftkeyActionButtonToConfirmQuizAnswer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void startQuizTimer() {
        QuizGameTimer quizTimer = new QuizGameTimer(QuizGameTimer.DEFAULT_TIME, QuizGameTimer.DEFAULT_TICK, lbl_remainingTime);
        quizTimer.startToListen(this.presenter);
        quizTimer.start();
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
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        saveViewData(instanceState);
    }

    private void saveViewData(Bundle instanceState) {
        instanceState.putString(LBL_QUESTION, lbl_question.getText().toString());
        instanceState.putString(LBL_QUESTION_VOCABLE, lbl_question_vocable.getText().toString());
        instanceState.putString(TXT_ANSWER, txt_answer.getText().toString());
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState.containsKey(LBL_QUESTION)) {
            lbl_question.setText(instanceState.getString(LBL_QUESTION));
        }
        if (instanceState.containsKey(LBL_QUESTION_VOCABLE)) {
            lbl_question_vocable.setText(instanceState.getString(LBL_QUESTION_VOCABLE));
        }
        if (instanceState.containsKey(TXT_ANSWER)) {
            txt_answer.setText(instanceState.getString(TXT_ANSWER));
        }
    }

    @Override
    public void confirmQuizAnswerAction() {
        String givenAnswer = txt_answer.getText().toString();
        presenter.onQuizAnswerFromView(givenAnswer);
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
    public void showQuizResultDialog(Quiz.FinalResult quizFinalResult, FormattedString message) {
        Drawable img_alertDialog;
        String quizResultTitle;
        switch (quizFinalResult) {
            case CORRECT:
                quizResultTitle = getString(R.string.correctAnswer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_correct, null);
                break;
            case WRONG:
                quizResultTitle = getString(R.string.wrongAnswer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_wrong, null);
                break;
            default:
                throw new RuntimeException("Unknown quiz result");
        }

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(quizResultTitle)
                .setMessage(localize(message) + "\n\n" + getString(R.string.msg_press_ok_to_continue))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.continueToPlay();
                    }
                })
                .setIcon(img_alertDialog);
        quizAlert = alertDialogBuilder.create();
        quizAlert.show();
    }

    @Override
    public void showGameResultDialog(FormattedString gameResultMessage) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.game_result))
                .setMessage(localize(gameResultMessage))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        quitGame();
                    }
                })
                .setCancelable(false);
        quizAlert = alertDialogBuilder.create();
        quizAlert.show();
    }

    @Override
    public void showErrorDialog(String msgErrorText) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.error))
                .setMessage(msgErrorText)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.abortGame();
                    }
                })
                .setCancelable(false);
        quizAlert = alertDialogBuilder.create();
        quizAlert.show();
    }

    @Override
    public void onBackPressed() {
        quitGame();
    }

    private void quitGame() {
        presenter.abortGame();
        presenter.detachView();
        finish();
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
