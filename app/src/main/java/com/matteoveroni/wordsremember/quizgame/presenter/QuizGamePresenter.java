package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameFindTranslationForVocableModel;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameModel;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Locale;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView> {

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

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
    public void detachView() {
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

    @Subscribe
    public void onEventQuizGenerated(EventQuizGenerated event) {
        view.setPojoUsed(event.getQuiz());
    }

    // TODO: refactoring of this method
    public void onQuizAnswerFromView(String answerFromView) {
        if (answerFromView.trim().isEmpty()) {
            view.showMessage(LocaleKey.MSG_ERROR_NO_ANSWER_GIVEN);
        } else {
            game.giveFinalAnswer(answerFromView);
            Quiz quiz = game.getCurrentQuiz();
            Quiz.FinalResult quizFinalResult = quiz.getFinalResult();

            FormattedString formattedString = new FormattedString();
            switch (quiz.getFinalResult()) {
                case RIGHT:
                    formattedString.setFormattedString("%s");
                    formattedString.setArgs(LocaleKey.CORRECT_ANSWER);
                    break;
                case WRONG:
                    formattedString.setFormattedString("%s");
                    formattedString.setArgs(LocaleKey.WRONG_ANSWER);
                    formattedString = formattedString.concat(new FormattedString("\n\n%s:\n", LocaleKey.CORRECT_ANSWERS));

                    List<String> rightAnswers = quiz.getRightAnswers();
                    for (int i = 0; i < rightAnswers.size(); i++) {
                        formattedString = formattedString.concat(new FormattedString(rightAnswers.get(i)));
                        if (i != rightAnswers.size() - 1) {
                            formattedString = formattedString.concat(new FormattedString(", "));
                        }
                    }
                    break;
            }
            view.showQuizResultDialog(quizFinalResult, formattedString);
        }
    }

    private void startNewQuizOrShowError() {
        view.clearAndHideFields();
        try {
            game.generateQuiz();
        } catch (NoMoreQuizzesException ex) {
            FormattedString gameResultMessage = new FormattedString(
                    "%s %s %d/%d %s",
                    LocaleKey.MSG_GAME_COMPLETED,
                    LocaleKey.SCORE,
                    game.getTotalScore(),
                    game.getNumberOfQuestions(),
                    LocaleKey.POINTS
            );
            view.showGameResultDialog(gameResultMessage);
        } catch (ZeroQuizzesException ex) {
            view.showErrorDialog(LocaleKey.MSG_ERROR_INSERT_SOME_VOCABLE);
        }
    }
}
