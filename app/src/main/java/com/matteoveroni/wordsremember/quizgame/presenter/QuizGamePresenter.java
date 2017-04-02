package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.Quiz;
import com.matteoveroni.wordsremember.quizgame.QuizGameModel;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView> {

    private QuizGameView view;
    private final QuizGameModel model;
    private final DictionaryDAO dao;

    private Quiz currentQuizInView;

    public QuizGamePresenter(QuizGameModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(QuizGameView view) {
        this.view = view;
        startNewQuizOrStopGameIfTheyAreFinished();
    }

    @Override
    public void destroy() {
        view = null;
    }

    private void startNewQuizOrStopGameIfTheyAreFinished() {
        try {
            currentQuizInView = model.getNextQuiz();
            view.setPojoUsed(currentQuizInView);
        } catch (NoMoreQuizzesException ex) {
            destroy();
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
        for (String rightAnswer : currentQuizInView.getRightAnswers()) {
            if (answer.equalsIgnoreCase(rightAnswer)) {
                return true;
            }
        }
        return false;
    }
}
