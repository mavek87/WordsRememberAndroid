package com.matteoveroni.wordsremember.quizgame.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameView extends View, PojoManipulable<Quiz> {

    void confirmQuizAnswerAction();

    void showErrorDialog(String msgErrorText);

    void showQuizResultDialog(Quiz.FinalResult quizFinalResult, String message);

    void showGameResultDialog(FormattedString localizedMessage);

    void clearAndHideFields();
}
