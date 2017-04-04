package com.matteoveroni.wordsremember.quizgame.presenter;

import android.widget.Toast;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.model.GameDifficulty;
import com.matteoveroni.wordsremember.quizgame.model.GameType;
import com.matteoveroni.wordsremember.quizgame.model.Quiz;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModel;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView> {

    private QuizGameView view;
    private final QuizGameModel model;
    private final DictionaryDAO dao;

    private Quiz currentQuiz;

    public QuizGamePresenter(DictionaryDAO dao) {
        this.dao = dao;
        this.model = new QuizGameModel(GameType.FIND_RIGHT_TRANSLATION_FOR_VOCABLES, GameDifficulty.EASY, dao);
    }

    @Override
    public void attachView(QuizGameView view) {
        this.model.registerToEventBus();
        this.view = view;
        //ToDo: new quiz should not start everytime the view is attached (eg after device rotation)
        startNewQuizOrStopGameIfTheyAreFinished();
    }

    @Override
    public void destroy() {
        this.model.unregisterToEventBus();
        this.view = null;
    }

    private void startNewQuizOrStopGameIfTheyAreFinished() {
        view.showMessage("New Quiz");
        try {
            currentQuiz = model.generateQuiz();
            view.setPojoUsed(currentQuiz);
        } catch (NoMoreQuizzesException ex) {
            view.showMessage("Game ended");
            view.returnToPreviousView();
        }
    }

    public void onQuizResponseFromView(String givenAnswer) {
        if (isAnswerCorrect(givenAnswer)) {
            // increment points
            view.showMessage("Right answer");
        } else {
            view.showMessage("Wrong answer");
        }
        startNewQuizOrStopGameIfTheyAreFinished();
    }

    private boolean isAnswerCorrect(String answer) {
        for (String rightAnswer : currentQuiz.getRightAnswers()) {
            if (answer.equalsIgnoreCase(rightAnswer)) {
                return true;
            }
        }
        return false;
    }
}
