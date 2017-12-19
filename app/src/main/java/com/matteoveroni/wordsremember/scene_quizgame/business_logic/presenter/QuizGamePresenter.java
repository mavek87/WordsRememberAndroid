package com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionTimer;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.QuizGameModel;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.QuizGameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionAnswerResult;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventGameModelInitialized;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizUpdatedWithNewQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.view.QuizGameView;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGamePresenter implements Presenter<QuizGameView>, QuestionTimer.TimerListener {

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final QuizGameModel quizModel;
    private QuizGameView view;
    private QuestionTimer questionTimer;
    private boolean isDialogShownInView = false;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.quizModel = new QuizGameModelFindTranslationForVocable(settings, dao);
    }

    private void startQuestionTimerCount() {
        if (questionTimer != null) {
            questionTimer.setTimerPrinter(view);
            questionTimer.addTimerListener(this);
            questionTimer.start();
        }
    }

    private void stopQuestionTimerCount() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }
    }

    private void pauseQuestionTimerCount() {
        if (questionTimer != null && !questionTimer.isPaused()) {
            questionTimer.pause();
        }
    }

    private void resetQuestionTimerCount() {
        stopQuestionTimerCount();
        questionTimer = new QuestionTimer(view, settings.getQuizGameQuestionTimerTotalTime(), settings.getQuizGameQuestionTimerTick());
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        this.view = quizGameView;
        EVENT_BUS.register(this);
        quizModel.startGame();

        if (!isDialogShownInView && questionTimer != null && questionTimer.isPaused())
            startQuestionTimerCount();
    }

    @Override
    public void detachView() {
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        quizModel.pauseGame();
        pauseQuestionTimerCount();
        view = null;
    }

    @Subscribe
    public void onEventGameModelInitialized(EventGameModelInitialized event) {
        makeNewQuestionOrShowErrorInView();
    }

    private void playNextQuestion() {
        resetQuestionTimerCount();
        makeNewQuestionOrShowErrorInView();
    }

    private void makeNewQuestionOrShowErrorInView() {
        view.clearAndHideFields();
        try {
            quizModel.generateQuestion();
        } catch (NoMoreQuizzesException ex) {
            handleNoMoreQuizzesException();
            view.hideKeyboard();
        } catch (ZeroQuizzesException ex) {
            // TODO: possible bug if the view is being cleared before..
            handleZeroQuizzesException();
        }
    }

    @Subscribe
    public void onEventNewQuestionAddToQuiz(EventQuizUpdatedWithNewQuestion event) {
        view.setPojoUsed(event.getQuiz());
        questionTimer = new QuestionTimer(view, settings.getQuizGameQuestionTimerTotalTime(), settings.getQuizGameQuestionTimerTick());
        startQuestionTimerCount();
    }

    private void handleNoMoreQuizzesException() {
        try {
            FormattedString gameResultMessage = new FormattedString(
                    "%s %s %d/%d %s",
                    LocaleKey.MSG_GAME_COMPLETED,
                    LocaleKey.SCORE,
                    quizModel.getFinalTotalScore(),
                    quizModel.getNumberOfQuestions(),
                    LocaleKey.POINTS
            );
            view.showGameResultDialog(gameResultMessage);
            isDialogShownInView = true;
        } catch (QuizGameModel.GameNotEndedYetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleZeroQuizzesException() {
        isDialogShownInView = true;
        view.showErrorDialog(LocaleKey.MSG_ERROR_INSERT_SOME_VOCABLE);
    }

    public void onQuestionAnswerFromView(String answerFromView) {
        String answer = StringUtils.strip(answerFromView);
        if (answer.isEmpty()) {
            view.showMessage(LocaleKey.MSG_ERROR_NO_ANSWER_GIVEN);
        } else {
            stopQuestionTimerCount();

            quizModel.answerCurrentQuestion(answer);

            Quiz currentQuiz = quizModel.getCurrentQuiz();
            Question currentQuestion = currentQuiz.getCurrentQuestion();

            isDialogShownInView = true;
            view.showQuestionResultDialog(currentQuestion.getQuestionAnswerResult(), buildQuizResultMessage(currentQuiz));
        }
    }

    private FormattedString buildQuizResultMessage(Quiz quiz) {
        Question currentQuestion = quiz.getCurrentQuestion();
        switch (currentQuestion.getQuestionAnswerResult()) {
            case CORRECT:
                return new FormattedString("%s\n" + currentQuestion, LocaleKey.MSG_CORRECT_ANSWER);
            case WRONG:
                FormattedString quizResultMessage = new FormattedString("%s\n\n%s:\n", LocaleKey.MSG_WRONG_ANSWER, LocaleKey.CORRECT_ANSWERS);

                Set<String> correctAnswers = currentQuestion.getCorrectAnswers();
                int index = 0;
                for (String answer : correctAnswers) {
                    quizResultMessage = quizResultMessage.concat(new FormattedString(answer));
                    if (index != correctAnswers.size() - 1) {
                        quizResultMessage = quizResultMessage.concat(new FormattedString(", "));
                    }
                    index++;
                }
                return quizResultMessage;
            default:
                throw new RuntimeException("Impossible to build quiz result message. Quiz final result not correct nor wrong");
        }
    }

    @Override
    public void onQuizTimeElapsed() {
        stopQuestionTimerCount();

        quizModel.getCurrentQuiz().forceQuestionAnswerResult(QuestionAnswerResult.WRONG);

        isDialogShownInView = true;
        view.showQuestionResultDialog(QuestionAnswerResult.WRONG, new FormattedString("Time elapsed"));
    }

    public void onQuizResultDialogConfirmation() {
        isDialogShownInView = false;
        playNextQuestion();
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
        stopQuestionTimerCount();
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        view = null;
        quizModel.abortGame();
    }

    public long getRemainingTimeForCurrentQuizInMillis() {
        return questionTimer.getRemainingTime();
    }

    public long getRemainingTimeForCurrentQuizInSeconds() {
        // TODO: check if its possible to remove the int casting
        return (int) (getRemainingTimeForCurrentQuizInMillis() / 1000);
    }
}
