package com.matteoveroni.wordsremember.quizgame.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.wordsremember.Settings;
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

public class QuizGameFindTranslationForVocableModel {

    public static final String TAG = TagGenerator.tag(QuizGameFindTranslationForVocableModel.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final Settings settings;

    final Set<Integer> extractedPositionsForQuiz = new HashSet<>();
    int numberOfQuestions;

    public QuizGameFindTranslationForVocableModel(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    public void registerToEventBus() {
        if (!EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.register(this);
        }
    }

    public void unregisterToEventBus() {
        if (EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.unregister(this);
        }
    }

    public void reset() {
        extractedPositionsForQuiz.clear();
        numberOfQuestions = 0;
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

        Log.d(TAG, "Max number of quizzes creatable are: " + numberOfQuestions);
        EVENT_BUS.post(new EventQuizModelInitialized());
    }

    public void startQuizGeneration() throws NoMoreQuizzesException, ZeroQuizzesException {
        if (numberOfQuestions <= 0) throw new ZeroQuizzesException();
        int vocablePosition = extractUniqueRandomVocablePosition();
        dao.asyncSearchVocableWithTranslationByOffsetCommand(vocablePosition);
    }

    private int extractUniqueRandomVocablePosition() throws NoMoreQuizzesException {
        int initialNumberOfExtractedPositionsForQuiz = extractedPositionsForQuiz.size();
        if (initialNumberOfExtractedPositionsForQuiz >= numberOfQuestions) {
            throw new NoMoreQuizzesException();
        }

        IntRange positionsRange = new IntRange(0, numberOfQuestions - 1);
        int randPosition;
        do {
            randPosition = Int.getRandomInteger(positionsRange);
            extractedPositionsForQuiz.add(randPosition);
        } while (extractedPositionsForQuiz.size() == initialNumberOfExtractedPositionsForQuiz);
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

        EVENT_BUS.post(new EventQuizGenerated(new Quiz(vocableQuestion, answers)));
    }

    private List<String> populateRightAnswers(List<Word> translations) {
        List<String> answers = new ArrayList<>();
        for (Word translation : translations) {
            answers.add(translation.getName());
        }
        return answers;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }
}
