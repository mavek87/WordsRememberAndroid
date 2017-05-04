package com.matteoveroni.wordsremember.quizgame.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;
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

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private Quiz quiz;
    private QuizGameView view;
    private final QuizGameFindTranslationForVocableModel model;

    private final Settings settings;
    // TODO: refactoring => encapsulate score attribute in model
    private int score = 0;

    private static boolean isGameAlreadyStarted = false;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.model = new QuizGameFindTranslationForVocableModel(settings, dao);
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        this.view = quizGameView;

        EVENT_BUS.register(this);
        model.registerToEventBus();

        if (!isGameAlreadyStarted) {
            initGame();
        }
    }

    private void initGame() {
        view.reset();
        model.reset();
        score = 0;
        isGameAlreadyStarted = true;
    }

    @Override
    public void destroy() {
        settings.saveLastGameDate();
        // TODO: remember to remove this "try catch block" in production code
        try {
            Log.i(TAG, "" + settings.getLastGameDate());
            view.showMessage("" + settings.getLastGameDate());
        } catch (Exception ex) {
        }
        //////////////////////////////////////////////////////////////////////////
        EVENT_BUS.unregister(this);
        this.model.unregisterToEventBus();
        this.view = null;
    }

    @Subscribe
    public void onEvent(EventQuizModelInitialized event) {
        tryToStartNewQuizOrShowError();
    }

    private void tryToStartNewQuizOrShowError() {
        try {
            model.startQuizGeneration();
        } catch (NoMoreQuizzesException ex) {
            view.showGameResultDialog(score, model.getNumberOfQuestions());
        } catch (ZeroQuizzesException ex) {
            view.showErrorDialog("Error", "Insert some vocable with translations to play a new game");
        }
    }

    @Subscribe
    public void onEvent(EventQuizGenerated event) {
        quiz = event.getQuiz();
        view.setPojoUsed(quiz);
    }

    public void onQuizResponseFromView(String givenAnswer) {
        if (isAnswerCorrect(givenAnswer)) {
            score++;
            view.showQuizResultDialog(QuizResult.RIGHT);
        } else {
            view.showQuizResultDialog(QuizResult.WRONG);
        }
    }

    public void onQuizContinueGameFromView() {
        tryToStartNewQuizOrShowError();
    }

    public void onCloseGame() {
        view.close();
        isGameAlreadyStarted = false;
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
