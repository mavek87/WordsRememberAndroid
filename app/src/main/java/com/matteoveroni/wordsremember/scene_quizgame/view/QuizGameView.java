package com.matteoveroni.wordsremember.scene_quizgame.view;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.AndroidLocaleKey;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.CompletedQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameQuestionTimer;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

/**
 * Created by Matteo Veroni
 */

public interface QuizGameView extends View, PojoManipulable<Quiz>, GameQuestionTimer.TimerPrinter {

    void answerQuestionAction();

    void showErrorDialog(String msgErrorText);

    void showErrorDialog(AndroidLocaleKey localeKeyMessage);

    void showQuestionResultDialog(CompletedQuestion.AnswerResult questionAnswerResult, FormattedString localizedMessage);

    void showGameResultDialog(FormattedString localizedMessage);

    void clearAndHideFields();

    void showKeyboard();

    void hideKeyboard();
}
