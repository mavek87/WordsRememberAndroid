package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizGenerated;
import com.matteoveroni.wordsremember.quizgame.events.EventQuizModelInitialized;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuizzesException;
import com.matteoveroni.wordsremember.quizgame.exceptions.ZeroQuizzesException;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;
import com.matteoveroni.wordsremember.settings.model.Settings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class QuizGameFindTranslationForVocableModel implements QuizGameModel {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final Settings settings;
    private UniqueRandomNumberGenerator uniqueRandIntGenerator;
    private final Set<Integer> extractedVocablesIndexes = new HashSet<>();

    private Quiz currentQuiz;
    private int numberOfVocablesWithTranslations;
    private int numberOfQuestions;
    private int quizNumber;
    private int totalScore;
    private boolean isGameStarted = false;

    public QuizGameFindTranslationForVocableModel(Settings settings, DictionaryDAO dao) {
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

    private void initGame() {
        extractedVocablesIndexes.clear();
        totalScore = 0;
        numberOfVocablesWithTranslations = 0;
        numberOfQuestions = 0;
        quizNumber = 0;
        dao.countDistinctVocablesWithTranslations();
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
    public void onEventCalculateNumberOfQuestions(EventCountDistinctVocablesWithTranslationsCompleted event) {
        numberOfVocablesWithTranslations = event.getNumberOfVocablesWithTranslation();
        if (numberOfVocablesWithTranslations > settings.getNumberOfQuestions()) {
            numberOfQuestions = settings.getNumberOfQuestions();
        } else {
            numberOfQuestions = numberOfVocablesWithTranslations;
        }

        int minNumber = 0;
        int maxNumber = numberOfVocablesWithTranslations - 1;
        int maxNumberOfExtractions = numberOfQuestions;
        uniqueRandIntGenerator = new UniqueRandomNumberGenerator(minNumber, maxNumber, maxNumberOfExtractions);

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
            int uniqueRandomNumber = uniqueRandIntGenerator.extractNext();
            dao.asyncSearchDistinctVocableWithTranslationByOffset(uniqueRandomNumber);
        } catch (UniqueRandomNumberGenerator.NoMoreUniqueRandNumberExtractableException ex) {
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
        String vocableQuestion = vocable.getName();

        List<String> rightAnswersForCurrentQuiz = new ArrayList<>();
        for (Word translation : event.getTranslations()) {
            rightAnswersForCurrentQuiz.add(translation.getName());
        }

        currentQuiz = new Quiz(quizNumber, numberOfQuestions, vocableQuestion, rightAnswersForCurrentQuiz);
        EVENT_BUS.post(new EventQuizGenerated(currentQuiz));
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
            currentQuiz.setFinalFinalResult(Quiz.FinalResult.RIGHT);
        } else {
            currentQuiz.setFinalFinalResult(Quiz.FinalResult.WRONG);
        }
    }

    @Override
    public int getTotalScore() {
        return totalScore;
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
