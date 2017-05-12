package com.matteoveroni.wordsremember.quizgame.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.FormattedLocaleString;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameView extends View, PojoManipulable<Quiz> {

    void confirmQuizAnswerAction();

    void showErrorDialog(String msgErrorTitle, String msgErrorText);

    void showQuizResultDialog(Quiz.Result quizResult);

    void showGameResultDialog(FormattedLocaleString localizedMessage);

    void clearAndHideFields();
}
