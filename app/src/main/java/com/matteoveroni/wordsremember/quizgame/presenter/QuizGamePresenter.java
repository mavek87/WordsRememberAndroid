package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.Settings;
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

    private Quiz quiz;
    private QuizGameView view;
    private final QuizGameModelFindTranslationForVocable model;
    private final Settings settings = new Settings();

    public QuizGamePresenter(DictionaryDAO dao) {
        this.settings.setDifficulty(QuizGameDifficulty.EASY);
        this.model = new QuizGameModelFindTranslationForVocable(settings, dao);
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
            view.showErrorDialog("Error", "Insert some vocable with translations to play a new game");
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
            view.showQuizResultDialog(QuizResult.RIGHT);
        } else {
            view.showQuizResultDialog(QuizResult.WRONG);
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
