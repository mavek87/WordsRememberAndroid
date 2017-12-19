package com.matteoveroni.wordsremember.scene_quizgame.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionTimer;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionAnswerResult;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameView extends View, PojoManipulable<Quiz>, QuestionTimer.TimerPrinter {

    void answerQuestionAction();

    void showErrorDialog(String msgErrorText);

    void showQuestionResultDialog(QuestionAnswerResult questionAnswerResult, FormattedString localizedMessage);

    void showGameResultDialog(FormattedString localizedMessage);

    void clearAndHideFields();

    void showKeyboard();

    void hideKeyboard();

    void quitGame();
}
