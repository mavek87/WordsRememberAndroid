package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.IntRange;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.myutils.UniqueRandomNumbersGenerator;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.QuestionCompleted;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInit;
import com.matteoveroni.wordsremember.scene_quizgame.events.EventQuizGameModelInitException;
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

public class GameModelFindTranslationForVocable implements GameModel, WebTranslatorListener {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final DictionaryDAO dictionary;

    private Quiz quiz;
    private GameRunningState runningState = new GameRunningState();
    private UniqueRandomNumbersGenerator uniqueRandIntGenerator;
    private int totalScore;

    public GameModelFindTranslationForVocable(Settings settings, DictionaryDAO dictionary) {
        this.settings = settings;
        this.dictionary = dictionary;
    }

    @Override
    public void start() {
        registerToEventBus();
        if (!runningState.isPaused()) {
            initQuizGame();
        }
        runningState.setStarted();
    }

    @Override
    public void pause() {
        runningState.setPaused();
        unregisterToEventBus();
    }

    @Override
    public void stop() {
        runningState.setStopped();
        unregisterToEventBus();
    }

    private void initQuizGame() {
        quiz = new Quiz();
        runningState.reset();
        totalScore = 0;
        dictionary.countDistinctVocablesWithTranslations();
    }

    @Subscribe(sticky = true)
    public void onEventCountDistinctVocablesWithTranslations(EventCountDistinctVocablesWithTranslationsCompleted event) {
        int numberOfVocablesWithATranslation = event.getNumberOfVocablesWithTranslation();

        int maxNumberOfQuestions;

        if (numberOfVocablesWithATranslation < settings.getNumberOfQuestions()) {
            maxNumberOfQuestions = numberOfVocablesWithATranslation;
        } else {
            maxNumberOfQuestions = settings.getNumberOfQuestions();
        }

        quiz.setTotalNumberOfQuestions(maxNumberOfQuestions);

        int maxQuestionIndex = numberOfVocablesWithATranslation - 1;
        if (maxQuestionIndex >= 0) {
            uniqueRandIntGenerator = new UniqueRandomNumbersGenerator(new IntRange(0, maxQuestionIndex), maxNumberOfQuestions);
            EVENT_BUS.post(new EventQuizGameModelInit());
        } else {
            stop();
            EVENT_BUS.post(new EventQuizGameModelInitException(new ZeroQuestionsException()));
        }
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
            dictionary.asyncSearchDistinctVocableWithTranslationByOffset(uniqueRandNumber);
        } catch (UniqueRandomNumbersGenerator.NoMoreUniqueRandNumberExtractableException ex) {
            runningState.setStopped();
            throw new NoMoreQuestionsException();
        }
    }

    @Subscribe
    public void onEventGetExtractedVocableId(EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted event) {
        long vocableId = event.getVocableWithTranslationFound();
        dictionary.asyncSearchVocableById(vocableId);
    }

    @Subscribe
    public void onEventGetExtractedVocable(EventAsyncSearchVocableCompleted event) {
        Word vocable = event.getVocable();
        dictionary.asyncSearchVocableTranslations(vocable);
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
            quiz.addQuestion(new Question(quizVocable.getName(), rightAnswersForCurrentQuiz));
            EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
        }
    }

    @Override
    public void onTranslationCompletedSuccessfully(List<Word> translationsFoundFromTheWeb) {
        //TODO check if online translatsions are broken at the moment

        Log.i(TagGenerator.tag(GameModelFindTranslationForVocable.class), "Translations found from the web: \n" + Json.getInstance().toJson(translationsFoundFromTheWeb));

        for (Word webTranslation : translationsFoundFromTheWeb) {
            quiz.addTrueAnswerForCurrentQuestion(webTranslation.getName());
        }

        EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
    }

    @Override
    public void onTranslationCompletedWithError(Throwable t) {
        //TODO check if online translatsions are broken at the moment
        Log.e(TagGenerator.tag(GameModelFindTranslationForVocable.class), t.getMessage());
//        currentQuiz = new Quiz(quizIndex, numberOfQuestions, quizVocable, rightAnswersForCurrentQuiz);
//        EVENT_BUS.post(new EventQuizUpdatedWithNewQuestion(quiz));
    }

    @Override
    public Quiz getCurrentQuiz() {
        return quiz;
    }

    @Override
    public QuestionCompleted answerCurrentQuestion(String answer, long responseTime) {
        QuestionCompleted questionCompleted = quiz.answerCurrentQuestion(answer, responseTime);
        switch (questionCompleted.getAnswerResult()) {
            case CORRECT:
                totalScore++;
                break;
        }
        return questionCompleted;
    }

    @Override
    public int getFinalTotalScore() {
        if (!runningState.isStopped()) runningState.setStopped();
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
