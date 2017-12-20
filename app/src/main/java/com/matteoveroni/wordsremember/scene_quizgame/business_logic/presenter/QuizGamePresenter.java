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
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInitialized;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizUpdatedWithNewQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.NoMoreQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.ZeroQuestionsException;
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
        pauseQuestionTimerCount();
        quizModel.pauseGame();
        EVENT_BUS.unregister(this);
        settings.saveLastGameDate();
        view = null;
    }

    @Subscribe
    public void onEventQuizGameModelInitialized(EventQuizGameModelInitialized event) {
        makeNewQuestionOrShowErrorInView();
    }

    private void makeNewQuestionOrShowErrorInView() {
        view.clearAndHideFields();
        try {
            quizModel.generateQuestion();
        } catch (NoMoreQuestionsException ex) {
            handleNoMoreQuestionsException();
            view.hideKeyboard();
        } catch (ZeroQuestionsException ex) {
            // TODO: possible bug if the view is being cleared before..
            handleZeroQuestionsException();
        }
    }

    @Subscribe
    public void onEventQuizUpdateWithNewQuestion(EventQuizUpdatedWithNewQuestion event) {
        view.setPojoUsed(event.getQuiz());
        questionTimer = new QuestionTimer(view, settings.getQuizGameQuestionTimerTotalTime(), settings.getQuizGameQuestionTimerTick());
        startQuestionTimerCount();
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
            QuestionAnswerResult questionAnswerResult = currentQuiz.getCurrentQuestion().getQuestionAnswerResult();
            FormattedString localizedQuestionResultMessage = buildQuestionResultMessage(currentQuestion);

            isDialogShownInView = true;
            view.showQuestionResultDialog(questionAnswerResult, localizedQuestionResultMessage);
        }
    }

    @Override
    public void onQuizTimeElapsed() {
        stopQuestionTimerCount();

        quizModel.getCurrentQuiz().forceQuestionAnswerResult(QuestionAnswerResult.WRONG);

        isDialogShownInView = true;
        view.showQuestionResultDialog(QuestionAnswerResult.WRONG, new FormattedString("Time elapsed"));
    }

    public void onConfirmQuizResultDialogAction() {
        isDialogShownInView = false;
        playNextQuestion();
        view.showKeyboard();
    }

    private void playNextQuestion() {
        resetQuestionTimerCount();
        makeNewQuestionOrShowErrorInView();
    }

    public void onConfirmErrorDialogAction() {
        isDialogShownInView = false;
        quizModel.stopGame();
    }

    public void onConfirmGameResultDialogAction() {
        isDialogShownInView = false;
        view.hideKeyboard();
        view.quitGame();
        destroyPresenter();
    }

    public long getRemainingTimeForCurrentQuizInMillis() {
        return questionTimer.getRemainingTime();
    }

    public long getRemainingTimeForCurrentQuizInSeconds() {
        // TODO: check if its possible to remove the int casting
        return (int) (getRemainingTimeForCurrentQuizInMillis() / 1000);
    }

    private void destroyPresenter() {
        stopQuestionTimerCount();
        settings.saveLastGameDate();
        EVENT_BUS.unregister(this);
        view = null;
        quizModel.stopGame();
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

    private void handleNoMoreQuestionsException() {
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

    private void handleZeroQuestionsException() {
        isDialogShownInView = true;
        view.showErrorDialog(LocaleKey.MSG_ERROR_INSERT_SOME_VOCABLE);
    }

    private FormattedString buildQuestionResultMessage(Question question) {
        switch (question.getQuestionAnswerResult()) {
            case CORRECT:
                return new FormattedString("%s\n" + question, LocaleKey.MSG_CORRECT_ANSWER);
            case WRONG:
                FormattedString quizResultMessage = new FormattedString("%s\n\n%s:\n", LocaleKey.MSG_WRONG_ANSWER, LocaleKey.CORRECT_ANSWERS);

                Set<String> correctAnswers = question.getCorrectAnswers();
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
}
