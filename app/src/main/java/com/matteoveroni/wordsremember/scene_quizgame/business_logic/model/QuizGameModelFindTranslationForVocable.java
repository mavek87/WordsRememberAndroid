package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.myutils.UniqueRandomNumbersGenerator;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInitialized;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizUpdatedWithNewQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.NoMoreQuestionsException;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.exceptions.ZeroQuestionsException;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.web.WebTranslator;
import com.matteoveroni.wordsremember.web.WebTranslatorListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelFindTranslationForVocable implements QuizGameModel, WebTranslatorListener {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final DictionaryDAO dao;

    private Quiz quiz;
    private QuizGameRunningState runningState = new QuizGameRunningState();
    private UniqueRandomNumbersGenerator uniqueRandIntGenerator;
    private int totalScore;

    public QuizGameModelFindTranslationForVocable(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    @Override
    public void startGame() {
        if (!runningState.isPaused()) {
            initQuizGame();
        }
        runningState.setStarted();
        registerToEventBus();
    }

    private void initQuizGame() {
        quiz = new Quiz();
        runningState.reset();
        totalScore = 0;
        dao.countDistinctVocablesWithTranslations();
    }

    @Override
    public void pauseGame() {
        runningState.setPaused();
        unregisterToEventBus();
    }

    @Override
    public void stopGame() {
        runningState.setStopped();
        unregisterToEventBus();
    }

    @Override
    public boolean isGameStopped() {
        return runningState.isStopped();
    }

    @Subscribe
    public void onEventCountDistinctVocablesWithTranslations(EventCountDistinctVocablesWithTranslationsCompleted event) {
        int maxNumberOfQuizQuestions;

        int nmbOfVocablesWithTranslations = event.getNumberOfVocablesWithTranslation();
        if (nmbOfVocablesWithTranslations > settings.getNumberOfQuestions()) {
            maxNumberOfQuizQuestions = settings.getNumberOfQuestions();
        } else {
            maxNumberOfQuizQuestions = nmbOfVocablesWithTranslations;
        }

        quiz.setTotalNumberOfQuestions(maxNumberOfQuizQuestions);

        int maxQuestionIndex = nmbOfVocablesWithTranslations - 1;
        int maxNumberOfExtractions = maxNumberOfQuizQuestions;
        uniqueRandIntGenerator = new UniqueRandomNumbersGenerator(0, maxQuestionIndex, maxNumberOfExtractions);
        EVENT_BUS.post(new EventQuizGameModelInitialized());
    }

    @Override
    public int getNumberOfQuestions() {
        return quiz.getTotalNumberOfQuestions();
    }

    void setNumberOfQuestions(int numberOfQuestions) {
        quiz.setTotalNumberOfQuestions(numberOfQuestions);
    }

    @Override
    public void generateQuestion() throws NoMoreQuestionsException, ZeroQuestionsException {
        if (quiz.getTotalNumberOfQuestions() <= 0) throw new ZeroQuestionsException();
        try {
            int uniqueRandNumber = uniqueRandIntGenerator.extractNext();
            dao.asyncSearchDistinctVocableWithTranslationByOffset(uniqueRandNumber);
        } catch (UniqueRandomNumbersGenerator.NoMoreUniqueRandNumberExtractableException ex) {
            runningState.setStopped();
            throw new NoMoreQuestionsException();
        }
    }

    @Subscribe
    public void onEventGetExtractedVocableId(EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted event) {
        long vocableId = event.getVocableWithTranslationFound();
        dao.asyncSearchVocableById(vocableId);
    }

    @Subscribe
    public void onEventGetExtractedVocable(EventAsyncSearchVocableCompleted event) {
        Word vocable = event.getVocable();
        dao.asyncSearchVocableTranslations(vocable);
    }

    @Subscribe
    public void onEventGetTranslationsForVocable(EventAsyncSearchVocableTranslationsCompleted event) {
        Word quizVocable = event.getVocable();
        Set<String> rightAnswersForCurrentQuiz = new HashSet<>();

        for (Word translation : event.getTranslations()) {
            rightAnswersForCurrentQuiz.add(translation.getName());
        }

        if (settings.isOnlineTranslationServiceEnabled()) {
            //TODO this code uses hardcoded languages! remove hardcoded translation
            WebTranslator.getInstance().translate(quizVocable, Locale.ENGLISH, Locale.ITALIAN, this);
        } else {
            Question question = new Question(quizVocable.getName(), rightAnswersForCurrentQuiz);
            quiz.addQuestion(question);
            EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
        }
    }

    @Override
    public void onTranslationCompletedSuccessfully(List<Word> translationsFoundFromTheWeb) {
        //TODO check if online translatsions are broken at the moment

        Log.i(TagGenerator.tag(QuizGameModelFindTranslationForVocable.class), "Translations found from the web: \n" + Json.getInstance().toJson(translationsFoundFromTheWeb));

        for (Word webTranslation : translationsFoundFromTheWeb) {
            quiz.addCorrectAnswerForCurrentQuestion(webTranslation.getName());
        }

        EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
    }

    @Override
    public void onTranslationCompletedWithError(Throwable t) {
        //TODO check if online translatsions are broken at the moment
        Log.e(TagGenerator.tag(QuizGameModelFindTranslationForVocable.class), t.getMessage());
//        currentQuiz = new Quiz(quizIndex, numberOfQuestions, quizVocable, rightAnswersForCurrentQuiz);
//        EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
    }

    @Override
    public Quiz getCurrentQuiz() {
        return quiz;
    }

    @Override
    public void answerCurrentQuestion(String answer) {
        quiz.answerCurrentQuestion(answer);
        switch (quiz.getCurrentQuestion().getQuestionAnswerResult()) {
            case CORRECT:
                totalScore++;
                break;
            case WRONG:
                break;
            case NOT_ANSWERED_YET:
                break;
        }
    }

    @Override
    public int getFinalTotalScore() throws GameNotEndedYetException {
        if (runningState.isStopped()) return totalScore;
        else throw new GameNotEndedYetException();
    }

    private void registerToEventBus() {
        if (!EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.register(this);
        }
    }

    private void unregisterToEventBus() {
        if (EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.unregister(this);
        }
    }
}
