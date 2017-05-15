package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.FormattedString;
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

    protected static final String LOCALE_KEY_ERROR = "error";
    protected static final String LOCALE_KEY_NO_ANSWER_GIVEN_MSG = "no_answer_given_msg";
    protected static final String LOCALE_KEY_GAME_COMPLETED_MSG = "game_completed_msg";
    protected static final String LOCALE_KEY_SCORE = "score";
    protected static final String LOCALE_KEY_POINTS = "points";

    private final Settings settings;
    private final QuizGameModel game;
    private QuizGameView view;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.game = new QuizGameFindTranslationForVocableModel(settings, dao);
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        this.view = quizGameView;
        EVENT_BUS.register(this);
        game.startGame();
    }

    @Override
    public void destroy() {
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        game.pauseGame();
        view = null;
    }

    public void abortGame() {
        game.abortGame();
    }

    public void continueToPlay() {
        startNewQuizOrShowError();
    }

    @Subscribe
    public void onEventModelInitialized(EventQuizModelInitialized event) {
        startNewQuizOrShowError();
    }

    private void startNewQuizOrShowError() {
        view.clearAndHideFields();
        try {
            game.generateQuiz();
        } catch (NoMoreQuizzesException ex) {
            view.showGameResultDialog(
                    new FormattedString(
                            "%s %s %d/%d %s",
                            LOCALE_KEY_GAME_COMPLETED_MSG,
                            LOCALE_KEY_SCORE,
                            game.getScore(),
                            game.getNumberOfQuestions(),
                            LOCALE_KEY_POINTS
                    ));
        } catch (ZeroQuizzesException ex) {
            // TODO: use a locale key
            view.showErrorDialog("Insert some vocable with translations to play a new game");
        }
    }

    @Subscribe
    public void onEventQuizGenerated(EventQuizGenerated event) {
        Quiz quiz = event.getQuiz();
        view.setPojoUsed(quiz);
    }

    public void onQuizAnswerFromView(String givenAnswer) {
        if (givenAnswer.trim().isEmpty()) {
            view.showMessage(LOCALE_KEY_NO_ANSWER_GIVEN_MSG);
        } else {
            Quiz.Result quizResult = game.checkAnswer(givenAnswer);
            view.showQuizResultDialog(quizResult);
        }
    }
}
