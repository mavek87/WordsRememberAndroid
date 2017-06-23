package com.matteoveroni.wordsremember.quizgame.business_logic.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizFinalAnswerChecker;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.myutils.UniqueRandomNumbersGenerator;
import com.matteoveroni.wordsremember.web.WebTranslator;
import com.matteoveroni.wordsremember.web.WebTranslatorListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Matteo Veroni
 */

public class QuizGameModelFindTranslationForVocable implements QuizGameModel, WebTranslatorListener {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final DictionaryDAO dao;
    private Quiz currentQuiz;
    private UniqueRandomNumbersGenerator uniqueRandIntGenerator;
    private int numberOfVocablesWithTranslations;
    private int numberOfQuestions;
    private int quizNumber;
    private int totalScore;
    private boolean isGameStarted = false;

    public QuizGameModelFindTranslationForVocable(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    @Override
    public void startGame() {
        if (!isGameStarted) {
            initGame();
            isGameStarted = true;
        }
        registerToEventBus();
    }

    @Override
    public void pauseGame() {
        unregisterToEventBus();
    }

    @Override
    public void abortGame() {
        isGameStarted = false;
        unregisterToEventBus();
    }

    @Subscribe
    public void onEventCountDistinctVocablesWithTranslations(EventCountDistinctVocablesWithTranslationsCompleted event) {
        numberOfVocablesWithTranslations = event.getNumberOfVocablesWithTranslation();
        if (numberOfVocablesWithTranslations > settings.getNumberOfQuestions()) {
            numberOfQuestions = settings.getNumberOfQuestions();
        } else {
            numberOfQuestions = numberOfVocablesWithTranslations;
        }

        int minNumber = 0;
        int maxNumber = numberOfVocablesWithTranslations - 1;
        int maxNumberOfExtractions = numberOfQuestions;
        uniqueRandIntGenerator = new UniqueRandomNumbersGenerator(minNumber, maxNumber, maxNumberOfExtractions);

        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    @Override
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    @Override
    public void generateQuiz() throws NoMoreQuizzesException, ZeroQuizzesException {
        if (numberOfQuestions <= 0) throw new ZeroQuizzesException();
        quizNumber++;
        try {
            int uniqueRandNumber = uniqueRandIntGenerator.extractNext();
            dao.asyncSearchDistinctVocableWithTranslationByOffset(uniqueRandNumber);
        } catch (UniqueRandomNumbersGenerator.NoMoreUniqueRandNumberExtractableException ex) {
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
        WebTranslator.getInstance().translate(vocable, Locale.ENGLISH, Locale.ITALIAN, this);
    }

    @Subscribe
    public void onEventGetTranslationsForVocable(EventAsyncSearchVocableTranslationsCompleted event) {
        Word vocable = event.getVocable();
        String vocableQuestion = vocable.getName();

        List<String> rightAnswersForCurrentQuiz = new ArrayList<>();
        for (Word translation : event.getTranslations()) {
            rightAnswersForCurrentQuiz.add(translation.getName());
        }

        currentQuiz = new Quiz(quizNumber, numberOfQuestions, vocableQuestion, rightAnswersForCurrentQuiz);
        EVENT_BUS.post(new EventQuizGenerated(currentQuiz));
    }

    @Override
    public void onTranslationCompletedSuccessfully(List<Word> translationsFound) {
        Log.i(TagGenerator.tag(QuizGameModelFindTranslationForVocable.class), "Translations found from the web: \n" + Json.getInstance().toJson(translationsFound));
    }

    @Override
    public void onTranslationCompletedWithError(Throwable t) {
        Log.i(TagGenerator.tag(QuizGameModelFindTranslationForVocable.class), t.getMessage());
    }

    @Override
    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    @Override
    public void giveFinalAnswer(String finalAnswer) {
        currentQuiz.setFinalAnswer(finalAnswer);
        if (QuizFinalAnswerChecker.isFinalAnswerCorrect(currentQuiz)) {
            totalScore++;
            currentQuiz.setFinalResult(Quiz.FinalResult.CORRECT);
        } else {
            currentQuiz.setFinalResult(Quiz.FinalResult.WRONG);
        }
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public void setCurrentQuizFinalResult(Quiz.FinalResult result) {
        currentQuiz.setFinalResult(result);
    }

    private void initGame() {
        numberOfVocablesWithTranslations = 0;
        numberOfQuestions = 0;
        quizNumber = 0;
        totalScore = 0;
        dao.countDistinctVocablesWithTranslations();
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