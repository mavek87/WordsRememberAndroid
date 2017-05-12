package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;
import com.matteoveroni.wordsremember.Settings;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModel;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView> {

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private QuizGameView view;
    private final QuizGameModel game;

    private final Settings settings;

    private static boolean isGameAlreadyStarted = false;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.game = new QuizGameFindTranslationForVocableModel(settings, dao);
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        this.view = quizGameView;

        EVENT_BUS.register(this);
        game.registerToEventBus();

        if (!isGameAlreadyStarted) {
            initGame();
        }
    }

    @Override
    public void destroy() {
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        this.game.unregisterToEventBus();
        this.view = null;
    }

    private void initGame() {
        game.init();
        isGameAlreadyStarted = true;
    }

    public void abortGame() {
        isGameAlreadyStarted = false;
        view.close();
    }

    public void continueGame() {
        startNewQuizOrShowError();
    }

    @Subscribe
    public void onEventModelInitialized(EventQuizModelInitialized event) {
        startNewQuizOrShowError();
    }

    private void startNewQuizOrShowError() {
        view.reset();
        try {
            game.generateQuiz();
        } catch (NoMoreQuizzesException ex) {
            view.showGameResultDialog(game.getScore(), game.getNumberOfQuestions());
        } catch (ZeroQuizzesException ex) {
            view.showErrorDialog("Error", "Insert some vocable with translations to play a new game");
        }
    }

    @Subscribe
    public void onEventQuizGenerated(EventQuizGenerated event) {
        Quiz quiz = event.getQuiz();
        view.setPojoUsed(quiz);
    }

    public void onQuizResponseFromView(String givenAnswer) {
        Quiz.Result quizResult = game.checkAnswer(givenAnswer);
        view.showQuizResultDialog(quizResult);
    }
}
