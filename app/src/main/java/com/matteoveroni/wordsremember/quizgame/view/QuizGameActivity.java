package com.matteoveroni.wordsremember.quizgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
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
        lbl_question.setText(question);
        currentQuiz = new Quiz(question, rightAnswers);
    }

    @Override
    public void returnToPreviousView() {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
    }

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
