package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.wordsremember.settings.model.Settings;
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

    private final Set<Integer> extractedVocablesIndexes = new HashSet<>();

    private boolean isGameStarted = false;

    private Quiz currentQuiz;
    private int numberOfQuestions;
    private int questionNumber;
    private int score;

    public QuizGameFindTranslationForVocableModel(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    @Override
    public int getQuestionNumber() {
        return questionNumber;
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

    private void initGame() {
        extractedVocablesIndexes.clear();
        score = 0;
        numberOfQuestions = 0;
        questionNumber = 0;
        adjustNumberOfQuizzesCountingMaxNumberOfQuizCreatable();
    }

    private void adjustNumberOfQuizzesCountingMaxNumberOfQuizCreatable() {
        dao.countDistinctVocablesWithTranslations();
    }

    @Subscribe
    public void onEventCalculateNumberOfQuestions(EventCountDistinctVocablesWithTranslationsCompleted event) {
        numberOfQuestions = event.getNumberOfVocablesWithTranslation();

        if (numberOfQuestions > settings.getNumberOfQuestions()) {
            numberOfQuestions = settings.getNumberOfQuestions();
        }

        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    @Override
    public void generateQuiz() throws NoMoreQuizzesException, ZeroQuizzesException {
        if (numberOfQuestions <= 0) throw new ZeroQuizzesException();

        questionNumber++;

        int vocablePosition = extractUniqueRandomVocablePosition();

        dao.asyncSearchDistinctVocableWithTranslationByOffsetCommand(vocablePosition);
    }

    private int extractUniqueRandomVocablePosition() throws NoMoreQuizzesException {
        int initialNumberOfExtractedVocablesIndexes = extractedVocablesIndexes.size();
        if (initialNumberOfExtractedVocablesIndexes >= numberOfQuestions) {
            throw new NoMoreQuizzesException();
        }

        IntRange positionsRange = new IntRange(0, numberOfQuestions - 1);
        int randPosition;
        do {
            randPosition = Int.getRandomInt(positionsRange);
            extractedVocablesIndexes.add(randPosition);
        } while (extractedVocablesIndexes.size() == initialNumberOfExtractedVocablesIndexes);
        return randPosition;
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

        List<Word> translations = event.getTranslations();
        List<String> answers = populateRightAnswers(translations);

        currentQuiz = new Quiz(questionNumber, numberOfQuestions, vocableQuestion, answers);
        EVENT_BUS.post(new EventQuizGenerated(currentQuiz));
    }

    private List<String> populateRightAnswers(List<Word> translations) {
        List<String> answers = new ArrayList<>();
        for (Word translation : translations) {
            answers.add(translation.getName());
        }
        return answers;
    }

    @Override
    public Quiz.Result checkAnswer(String answer) {
        if (currentQuiz == null)
            throw new RuntimeException("Unexpected exception. No current quiz set in QuizGame model");

        QuizAnswerChecker answerChecker = new QuizAnswerChecker(currentQuiz);
        if (answerChecker.isAnswerCorrect(answer)) {
            score++;
            return Quiz.Result.RIGHT;
        } else {
            return Quiz.Result.WRONG;
        }
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
