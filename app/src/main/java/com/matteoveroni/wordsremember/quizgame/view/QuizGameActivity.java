package com.matteoveroni.wordsremember.quizgame.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.pojos.QuizResult;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenter;
import com.matteoveroni.wordsremember.quizgame.presenter.QuizGamePresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matteo Veroni
 */

public class QuizGameActivity extends ActivityView implements QuizGameView, LoaderManager.LoaderCallbacks<QuizGamePresenter> {

    private QuizGamePresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    @BindView(R.id.quiz_game_question_text)
    TextView lbl_question;

    @BindView(R.id.quiz_game_answer_edit_text)
    EditText txt_answer;

    @BindView(R.id.quiz_game_accept_answer_button)
    Button btn_acceptAnswer;

    private Quiz currentQuiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        ButterKnife.bind(this);
        setupAndShowToolbar();
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            final String title = Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", getString(R.string.title_activity_quiz_game));
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
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
    public void showQuizResult(QuizResult result) {
        String resultMessage;
        switch (result) {
            case RIGHT:
                resultMessage = "Correct answer press ok to go to the next question";
                break;
            case WRONG:
                resultMessage = "Wrong answer press ok to go to the next question";
                break;
            default:
                throw new RuntimeException("Unknown quiz result");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Quiz Result")
                .setMessage(resultMessage)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.onQuizContinueGameFromView();
                    }
                });
        AlertDialog resultDialog = alertDialogBuilder.create();
        resultDialog.show();
    }

    @OnClick(R.id.quiz_game_accept_answer_button)
    public void onButtonAcceptAnswerAction() {
        final String givenAnswer = txt_answer.getText().toString();
        if (givenAnswer.trim().isEmpty()) {
            Toast.makeText(this, "Empty answer. Insert an answer and continue.", Toast.LENGTH_SHORT).show();
        } else {
            presenter.onQuizResponseFromView(givenAnswer);
        }
    }

    @Override
    public Quiz getPojoUsed() {
        return currentQuiz;
    }

    @Override
    public void setPojoUsed(Quiz pojo) {
        final String question = pojo.getQuestion();
        final List<String> rightAnswers = pojo.getRightAnswers();
        Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
        lbl_question.setText(question);
        currentQuiz = new Quiz(question, rightAnswers);
//        showViewFields(true);
    }

    @Override
    public void returnToPreviousView() {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
    }

    @Override
    public void reset() {
//        lbl_question.setText("");
        txt_answer.setText("");
//        showViewFields(false);
    }

//    private void showViewFields(boolean areViewFieldsVisible) {
//        int visibility;
//        if (areViewFieldsVisible) {
//            visibility = View.VISIBLE;
//        } else {
//            visibility = View.INVISIBLE;
//        }
//        lbl_question.setVisibility(visibility);
//        txt_answer.setVisibility(visibility);
//        btn_acceptAnswer.setVisibility(visibility);
//    }

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
        presenter = null;
    }
}
