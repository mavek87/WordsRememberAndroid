package com.matteoveroni.wordsremember.quizgame.view;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matteo Veroni
 */

public class QuizGameActivity extends ActivityView implements QuizGameView, LoaderManager.LoaderCallbacks<QuizGamePresenter> {

    @BindView(R.id.quiz_game_question)
    TextView lbl_question;

    @BindView(R.id.quiz_game_question_vocable)
    TextView lbl_question_vocable;

    @BindView(R.id.quiz_game_answer_edit_text)
    EditText txt_answer;

    private static final String LBL_QUESTION_KEY = "lbl_question_key";
    private static final String LBL_QUESTION_VOCABLE_KEY = "lbl_question_vocable_key";
    private static final String LBL_ANSWER_KEY = "txt_answer_key";

    private Quiz currentQuiz;
    private AlertDialog quizAlert;
    private AlertDialog.Builder alertDialogBuilder;

    private QuizGamePresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    @Override
    public Loader<QuizGamePresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new QuizGamePresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<QuizGamePresenter> loader, QuizGamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<QuizGamePresenter> loader) {
        this.presenter = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        presenter.destroy();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        ButterKnife.bind(this);
        setupAndShowToolbar();

        if (savedInstanceState != null) restoreViewData(savedInstanceState);

        // Associate softkey action button to confirmQuizAnswerAction()
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            String title = Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", getString(R.string.title_activity_quiz_game));
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        saveViewData(instanceState);
    }

    private void saveViewData(Bundle instanceState) {
        instanceState.putString(LBL_QUESTION_KEY, lbl_question.getText().toString());
        instanceState.putString(LBL_QUESTION_VOCABLE_KEY, lbl_question_vocable.getText().toString());
        instanceState.putString(LBL_ANSWER_KEY, txt_answer.getText().toString());
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState.containsKey(LBL_QUESTION_KEY)) {
            lbl_question.setText(instanceState.getString(LBL_QUESTION_KEY));
        }
        if (instanceState.containsKey(LBL_QUESTION_VOCABLE_KEY)) {
            lbl_question_vocable.setText(instanceState.getString(LBL_QUESTION_VOCABLE_KEY));
        }
        if (instanceState.containsKey(LBL_ANSWER_KEY)) {
            txt_answer.setText(instanceState.getString(LBL_ANSWER_KEY));
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
        lbl_question.setText(getString(R.string.translate_vocable_in_quiz_question) + " " + quiz.getQuizNumber() + "/" + quiz.getTotalNumberOfQuizzes());
        lbl_question_vocable.setText(currentQuiz.getQuestion());
        showAllViewFields(true);
    }

    @Override
    public void showQuizResultDialog(Quiz.Result quizResult) {
        // TODO: use string resources not fixed strings
        Drawable img_alertDialog;
        String quizResultTitle;
        switch (quizResult) {
            case RIGHT:
                quizResultTitle = getString(R.string.correct_answer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_correct, null);
                break;
            case WRONG:
                quizResultTitle = getString(R.string.wrong_answer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_wrong, null);
                break;
            default:
                throw new RuntimeException("Unknown quiz result");
        }

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(quizResultTitle)
                .setMessage(getString(R.string.press_ok_to_continue))
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
        presenter.destroy();
        finish();
    }

    @Override
    public void clearAndHideFields() {
        lbl_question.setText("");
        txt_answer.setText("");
        showAllViewFields(false);
    }

    private void showAllViewFields(boolean isViewVisible) {
        int visibility = isViewVisible ? View.VISIBLE : View.INVISIBLE;
        lbl_question.setVisibility(visibility);
        lbl_question_vocable.setVisibility(visibility);
        txt_answer.setVisibility(visibility);
    }
}
