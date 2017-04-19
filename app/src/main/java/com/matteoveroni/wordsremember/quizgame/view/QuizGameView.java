package com.matteoveroni.wordsremember.quizgame.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.pojos.QuizResult;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameView extends View, PojoManipulable<Quiz> {

    void showErrorDialog(String msgErrorTitle, String msgErrorText);

    void showQuizResultDialog(QuizResult result);

    void showGameResultDialog(int points);

    void onButtonAcceptAnswerAction();

    void returnToPreviousView();

    void reset();
}
