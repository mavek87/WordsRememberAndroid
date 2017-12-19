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
import com.matteoveroni.wordsremember.scene_quizgame.events.EventGameModelInitialized;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizUpdatedWithNewQuestion;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.scene_quizgame.exceptions.ZeroQuizzesException;
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
    private UniqueRandomNumbersGenerator uniqueRandIntGenerator;
    private int numberOfQuestions;
    private int totalScore;
    private boolean isGameStarted = false;
    private boolean isGameEnded = false;

    private final Set<String> rightAnswersForCurrentQuiz = new HashSet<>();
    private String quizVocable;

    public QuizGameModelFindTranslationForVocable(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    @Override
    public void startGame() {
        if (!isGameStarted) {
            initGame();
            isGameStarted = true;
            isGameEnded = false;
        }
        registerToEventBus();
    }

    private void initGame() {
        quizVocable = "";
        numberOfQuestions = 0;
        totalScore = 0;
        dao.countDistinctVocablesWithTranslations();
        quiz = new Quiz();
    }

    @Override
    public void pauseGame() {
        unregisterToEventBus();
    }

    @Override
    public void abortGame() {
        isGameStarted = false;
        isGameEnded = true;
        unregisterToEventBus();
    }

    @Override
    public boolean isGameEnded() {
        return isGameEnded;
    }

    @Subscribe
    public void onEventCountDistinctVocablesWithTranslations(EventCountDistinctVocablesWithTranslationsCompleted event) {
        int nmbOfVocablesWithTranslations = event.getNumberOfVocablesWithTranslation();
        if (nmbOfVocablesWithTranslations > settings.getNumberOfQuestions()) {
            numberOfQuestions = settings.getNumberOfQuestions();
        } else {
            numberOfQuestions = nmbOfVocablesWithTranslations;
        }

        quiz.setTotalNumberOfQuestions(numberOfQuestions);

        int minNumber = 0;
        int maxNumber = nmbOfVocablesWithTranslations - 1;
        int maxNumberOfExtractions = numberOfQuestions;
        uniqueRandIntGenerator = new UniqueRandomNumbersGenerator(minNumber, maxNumber, maxNumberOfExtractions);

        EVENT_BUS.post(new EventGameModelInitialized());
    }

    @Override
    public int getNumberOfQuestions() {
        return quiz.getTotalNumberOfQuestions();
    }

    void setNumberOfQuestions(int numberOfQuestions) {
        quiz.setTotalNumberOfQuestions(numberOfQuestions);
    }

    @Override
    public void generateQuestion() throws NoMoreQuizzesException, ZeroQuizzesException {
//        rightAnswersForCurrentQuiz.clear();

        if (numberOfQuestions <= 0) throw new ZeroQuizzesException();
        try {
            int uniqueRandNumber = uniqueRandIntGenerator.extractNext();
            dao.asyncSearchDistinctVocableWithTranslationByOffset(uniqueRandNumber);
        } catch (UniqueRandomNumbersGenerator.NoMoreUniqueRandNumberExtractableException ex) {
            isGameEnded = true;
            throw new NoMoreQuizzesException();
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
        Word vocable = event.getVocable();
        quizVocable = vocable.getName();

        for (Word translation : event.getTranslations()) {
            rightAnswersForCurrentQuiz.add(translation.getName());
        }

        if (settings.isOnlineTranslationServiceEnabled()) {
            WebTranslator.getInstance().translate(vocable, Locale.ENGLISH, Locale.ITALIAN, this);
        } else {
            Question question = new Question(quizVocable, rightAnswersForCurrentQuiz);
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
        if (isGameEnded) return totalScore;
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
