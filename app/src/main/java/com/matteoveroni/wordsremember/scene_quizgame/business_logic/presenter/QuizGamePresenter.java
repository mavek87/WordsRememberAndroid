package com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionTimer;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.gamemodel.GameModel;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.gamemodel.GameModelFindTranslationForVocable;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.QuestionAnswerResult;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInit;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInitException;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizUpdatedWithNewQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.NoMoreQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.ZeroQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.view.QuizGameView;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGamePresenter extends BasePresenter<QuizGameView> implements QuestionTimer.TimerListener {

    public static final String TAG = TagGenerator.tag(QuizGamePresenter.class);

    private final Settings settings;
    private final GameModel gameModel;
    private QuestionTimer questionTimer;
    private boolean isDialogShownInView = false;

    public QuizGamePresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.gameModel = new GameModelFindTranslationForVocable(settings, dao);
    }

    @Override
    public void attachView(QuizGameView quizGameView) {
        super.attachView(quizGameView);

        gameModel.start();

        if (!isDialogShownInView && questionTimer != null && questionTimer.isPaused())
            startQuestionTimerCount();
    }

    @Override
    public void detachView() {
        pauseQuestionTimerCount();
        gameModel.pause();
        settings.saveLastGameDate();
        super.detachView();
    }

    @Subscribe
    public void onEventQuizGameModelInit(EventQuizGameModelInit event) {
        makeNewQuestionOrShowErrorInView();
    }

    @Subscribe
    public void onEventQuizGameModelInitException(EventQuizGameModelInitException event) {
        handleNoMoreQuestionsException();
        view.hideKeyboard();
    }

    private void makeNewQuestionOrShowErrorInView() {
        view.clearAndHideFields();
        try {
            gameModel.generateQuestion();
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
        Quiz quizUpdated = event.getQuiz();
        view.setPojoUsed(quizUpdated);
        questionTimer = new QuestionTimer(view, settings.getQuizGameQuestionTimerTotalTime(), settings.getQuizGameQuestionTimerTick());
        startQuestionTimerCount();
    }

    public void onQuestionAnswerFromView(String answerFromView) {
        String answer = StringUtils.strip(answerFromView);
        if (answer.isEmpty()) {
            view.showMessage(LocaleKey.MSG_ERROR_NO_ANSWER_GIVEN);
        } else {
            stopQuestionTimerCount();

            gameModel.answerCurrentQuestion(answer);

            Quiz currentQuiz = gameModel.getCurrentQuiz();
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

        gameModel.getCurrentQuiz().forceQuestionAnswerResult(QuestionAnswerResult.WRONG);

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
        gameModel.stop();
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
        gameModel.stop();
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
        FormattedString gameResultMessage = new FormattedString(
                "%s %s %d/%d %s",
                LocaleKey.MSG_GAME_COMPLETED,
                LocaleKey.SCORE,
                gameModel.getFinalTotalScore(),
                gameModel.getNumberOfQuestions(),
                LocaleKey.POINTS
        );
        isDialogShownInView = true;
        view.showGameResultDialog(gameResultMessage);
    }

    private void handleZeroQuestionsException() {
        isDialogShownInView = true;
        view.showErrorDialog(LocaleKey.MSG_ERROR_INSERT_SOME_VOCABLE);
    }

    private FormattedString buildQuestionResultMessage(Question question) {
        switch (question.getQuestionAnswerResult()) {
            case CORRECT:
                return new FormattedString("%s\n\n" + question.getGivenAnswer(), LocaleKey.MSG_CORRECT_ANSWER);
            case WRONG:
                FormattedString quizResultMessage = new FormattedString("%s\n\n%s:\n\n", LocaleKey.MSG_WRONG_ANSWER, LocaleKey.CORRECT_ANSWERS);

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
