package com.matteoveroni.wordsremember.quizgame.business_logic.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizTimer;
import com.matteoveroni.wordsremember.quizgame.business_logic.model.QuizGameModel;
import com.matteoveroni.wordsremember.quizgame.business_logic.model.QuizGameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.quizgame.events.EventGameModelInitialized;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameView;
import com.matteoveroni.wordsremember.settings.model.Settings;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView>, QuizTimer.TimerListener {

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final QuizGameModel quizModel;
    private QuizGameView view;
    private QuizTimer quizTimer;
    private boolean isDialogShownInView = false;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.quizModel = new QuizGameModelFindTranslationForVocable(settings, dao);
    }

    private void startQuizTimerCount() {
        if (quizTimer != null) {
            quizTimer.setTimerPrinter(view);
            quizTimer.addTimerListener(this);
            quizTimer.start();
        }
    }

    private void stopQuizTimerCount() {
        if (quizTimer != null) {
            quizTimer.cancel();
        }
    }

    private void pauseQuizTimerCount() {
        if (quizTimer != null && !quizTimer.isPaused()) {
            quizTimer.pause();
        }
    }

    private void resetQuizTimerCount() {
        stopQuizTimerCount();
        quizTimer = new QuizTimer(view, settings.getQuizGameTimerTotalTime(), settings.getQuizGameTimerTick());
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        this.view = quizGameView;
        EVENT_BUS.register(this);
        quizModel.startGame();

        if (!isDialogShownInView && quizTimer != null && quizTimer.isPaused())
            startQuizTimerCount();
    }

    @Override
    public void detachView() {
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        quizModel.pauseGame();
        pauseQuizTimerCount();
        view = null;
    }

    @Subscribe
    public void onEventGameModelInitialized(EventGameModelInitialized event) {
        startNewQuizOrShowErrorInView();
    }

    private void playNextQuiz() {
        resetQuizTimerCount();
        startNewQuizOrShowErrorInView();
    }

    private void startNewQuizOrShowErrorInView() {
        view.clearAndHideFields();
        try {
            quizModel.generateQuiz();
        } catch (NoMoreQuizzesException ex) {
            handleNoMoreQuizzesException();
        } catch (ZeroQuizzesException ex) {
            // TODO: possible bug if the view is being cleared before..
            handleZeroQuizzesException();
        }
    }

    @Subscribe
    public void onEventNewQuizGenerated(EventQuizGenerated event) {
        view.setPojoUsed(event.getQuiz());
        quizTimer = new QuizTimer(view, settings.getQuizGameTimerTotalTime(), settings.getQuizGameTimerTick());
        startQuizTimerCount();
    }

    private void handleNoMoreQuizzesException() {
        isDialogShownInView = true;
        FormattedString gameResultMessage = new FormattedString(
                "%s %s %d/%d %s",
                LocaleKey.MSG_GAME_COMPLETED,
                LocaleKey.SCORE,
                quizModel.getTotalScore(),
                quizModel.getNumberOfQuestions(),
                LocaleKey.POINTS
        );
        view.showGameResultDialog(gameResultMessage);
    }

    private void handleZeroQuizzesException() {
        isDialogShownInView = true;
        view.showErrorDialog(LocaleKey.MSG_ERROR_INSERT_SOME_VOCABLE);
    }

    public void onQuizAnswerFromView(String answerFromView) {
        String answer = StringUtils.strip(answerFromView);
        if (answer.isEmpty()) {
            view.showMessage(LocaleKey.MSG_ERROR_NO_ANSWER_GIVEN);
        } else {
            stopQuizTimerCount();

            quizModel.giveFinalAnswer(answer);
            Quiz quiz = quizModel.getCurrentQuiz();
            Quiz.FinalResult quizFinalResult = quiz.getFinalResult();

            isDialogShownInView = true;
            view.showQuizResultDialog(quizFinalResult, buildQuizResultMessage(quiz));
        }
    }

    private FormattedString buildQuizResultMessage(Quiz quiz) {
        FormattedString quizResultMessage = new FormattedString();
        switch (quiz.getFinalResult()) {
            case CORRECT:
                quizResultMessage.setFormattedString("%s\n" + quiz.getFinalAnswer());
                quizResultMessage.setArgs(LocaleKey.MSG_CORRECT_ANSWER);
                break;
            case WRONG:
                quizResultMessage = quizResultMessage.concat(
                        new FormattedString("%s\n\n%s:\n", LocaleKey.MSG_WRONG_ANSWER, LocaleKey.CORRECT_ANSWERS)
                );

                Set<String> correctAnswers = quiz.getRightAnswers();
                int index = 0;
                for (String answer : correctAnswers) {
                    quizResultMessage = quizResultMessage.concat(new FormattedString(answer));
                    if (index != correctAnswers.size() - 1) {
                        quizResultMessage = quizResultMessage.concat(new FormattedString(", "));
                    }
                    index++;
                }
                break;
        }
        return quizResultMessage;
    }

    @Override
    public void onQuizTimeElapsed() {
        stopQuizTimerCount();

        quizModel.setCurrentQuizFinalResult(Quiz.FinalResult.WRONG);

        isDialogShownInView = true;
        view.showQuizResultDialog(Quiz.FinalResult.WRONG, new FormattedString("Time elapsed"));
    }

    public void onQuizResultDialogConfirmation() {
        isDialogShownInView = false;
        playNextQuiz();
        view.showKeyboard();
    }

    public void onErrorDialogConfirmation() {
        isDialogShownInView = false;
        quizModel.abortGame();
    }

    public void onGameResultDialogConfirmation() {
        isDialogShownInView = false;
        view.hideKeyboard();
        view.quitGame();
        destroyPresenter();
    }

    private void destroyPresenter() {
        stopQuizTimerCount();
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        view = null;
        quizModel.abortGame();
    }

    public long getRemainingTimeForCurrentQuizInMillis() {
        return quizTimer.getRemainingTime();
    }

    public long getRemainingTimeForCurrentQuizInSeconds() {
        // TODO: check if its possible to remove the int casting
        return (int) (getRemainingTimeForCurrentQuizInMillis() / 1000);
    }
}
