package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.model.GameDifficulty;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.QuizResult;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView> {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private QuizGameView view;
    private final QuizGameFindTranslationForVocableModel model;

    private Quiz currentQuiz;

    public QuizGamePresenter(DictionaryDAO dao) {
        this.model = new QuizGameFindTranslationForVocableModel(GameDifficulty.EASY, dao);
    }

    @Override
    public void attachView(QuizGameView view) {
        EVENT_BUS.register(this);
        this.model.registerToEventBus();
        this.view = view;
    }

    @Override
    public void destroy() {
        EVENT_BUS.unregister(this);
        this.model.unregisterToEventBus();
        this.view = null;
    }

    @Subscribe
    public void onEvent(EventQuizModelInitialized event) {
        startNewQuizOrStopGameIfTheyAreFinished();
    }

    private void startNewQuizOrStopGameIfTheyAreFinished() {
        view.showMessage("New Quiz");
        try {
            model.startQuizGeneration();
        } catch (NoMoreQuizzesException ex) {
            endGame();
        }
    }

    public void onQuizEndGame() {
        endGame();
    }

    private void endGame() {
        view.showMessage("Game ended");
        view.returnToPreviousView();
    }

    @Subscribe
    public void onEvent(EventQuizGenerated event) {
        currentQuiz = event.getQuiz();
        view.setPojoUsed(currentQuiz);
    }

    public void onQuizResponseFromView(String givenAnswer) {
        if (isAnswerCorrect(givenAnswer)) {
            // increment points
            view.showQuizResult(QuizResult.RIGHT);
        } else {
            view.showQuizResult(QuizResult.WRONG);
        }
    }

    public void onQuizContinueGameFromView() {
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
