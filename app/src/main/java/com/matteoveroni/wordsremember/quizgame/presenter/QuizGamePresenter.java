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
    private final QuizGameModel model;

    private final Settings settings;

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
        model.reset();
        isGameAlreadyStarted = true;
    }

    @Override
    public void destroy() {
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        this.model.unregisterToEventBus();
        this.view = null;
    }

    @Subscribe
    public void onEvent(EventQuizModelInitialized event) {
        tryToStartNewQuizOrShowError();
    }

    private void tryToStartNewQuizOrShowError() {
        view.reset();
        try {
            model.generateQuiz();
        } catch (NoMoreQuizzesException ex) {
            view.showGameResultDialog(model.getScore(), model.getNumberOfQuestions());
        } catch (ZeroQuizzesException ex) {
            view.showErrorDialog("Error", "Insert some vocable with translations to play a new game");
        }
    }

    @Subscribe
    public void onEvent(EventQuizGenerated event) {
        quiz = event.getQuiz();
        view.setPojoUsed(quiz);
    }

    public void onContinueQuizGame() {
        tryToStartNewQuizOrShowError();
    }

    public void onCloseGame() {
        view.close();
        isGameAlreadyStarted = false;
    }

    public void onQuizResponseFromView(String givenAnswer) {
        if (isAnswerCorrect(givenAnswer)) {
            model.increaseScore();
            view.showQuizResultDialog(QuizResult.RIGHT);
        } else {
            view.showQuizResultDialog(QuizResult.WRONG);
        }
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
