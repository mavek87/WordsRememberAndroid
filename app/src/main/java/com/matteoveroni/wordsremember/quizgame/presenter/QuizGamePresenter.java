package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameSessionSettings;
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

    private QuizGameSessionSettings settings = new QuizGameSessionSettings();

    private Quiz quiz;
    private QuizGameView view;
    private final QuizGameFindTranslationForVocableModel model;

    public QuizGamePresenter(DictionaryDAO dao) {
        this.model = new QuizGameFindTranslationForVocableModel(settings, dao);
    }

    @Override
    public void attachView(QuizGameView view) {
        EVENT_BUS.register(this);
        this.model.reset();
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
        view.reset();
        try {
            model.startQuizGeneration();
        } catch (NoMoreQuizzesException ex) {
            endGame();
        } catch (ZeroQuizzesException ex) {
            view.showMessage("Insert some vocable with translations to play a new game");
        }
    }

    @Subscribe
    public void onEvent(EventQuizGenerated event) {
        quiz = event.getQuiz();
        view.setPojoUsed(quiz);
    }

    public void onQuizEndGame() {
        endGame();
    }

    private void endGame() {
        view.returnToPreviousView();
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
        for (String rightAnswer : quiz.getRightAnswers()) {
            if (answer.equalsIgnoreCase(rightAnswer)) {
                return true;
            }
        }
        return false;
    }
}
