package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.QuestionAnswerChecker;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.QuestionCompleted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final Map<Integer, Question> questions = new HashMap<>();
    private final List<QuestionCompleted> correctQuestions = new ArrayList<>();
    private final List<QuestionCompleted> wrongQuestions = new ArrayList<>();
    private final QuestionAnswerChecker questionAnswerChecker = new QuestionAnswerChecker();
    private final GameDifficulty gameDifficulty;

    private int questionsIndex = 0;
    private int totalNumberOfQuestions;
    private long totalResponseTime;
    private long averageResponseTime;

    public Quiz(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
        this.totalNumberOfQuestions = this.gameDifficulty.getId() * GameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public int getQuestionIndex() {
        return questionsIndex;
    }

    public int getTotalNumberOfQuestions() {
        return totalNumberOfQuestions;
    }

    public void setTotalNumberOfQuestions(int numberOfQuestion) {
        totalNumberOfQuestions = numberOfQuestion;
    }

    public Question getCurrentQuestion() {
        return questions.get(questionsIndex);
    }

    public List<QuestionCompleted> getCorrectQuestions() {
        return correctQuestions;
    }

    public List<QuestionCompleted> getWrongQuestions() {
        return wrongQuestions;
    }

    public long getTotalResponseTime() {
        return totalResponseTime;
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void addQuestion(Question question) {
        questions.put(questionsIndex, question);
        questionsIndex++;
    }

    public void addTrueAnswerForCurrentQuestion(String answer) {
        if (!answer.trim().isEmpty()) {
            questions.get(questionsIndex).addTrueAnswer(answer);
        }
    }

    public QuestionCompleted answerCurrentQuestion(String givenAnswer, long responseTime) {
        return answerQuestion(getCurrentQuestion(), givenAnswer, responseTime);
    }

    public QuestionCompleted forceQuestionAnswerResult(QuestionCompleted.AnswerResult result, long responseTime) {
        return answerQuestion(getCurrentQuestion(), "", result, responseTime);
    }

    private QuestionCompleted answerQuestion(Question question, String givenAnswer, long responseTime) {
        final QuestionCompleted.AnswerResult result = questionAnswerChecker.checkAnswerResultForQuestion(givenAnswer, question);
        return answerQuestion(question, givenAnswer, result, responseTime);
    }

    private QuestionCompleted answerQuestion(Question question, String givenAnswer, QuestionCompleted.AnswerResult result, long responseTime) {
        QuestionCompleted questionCompleted = new QuestionCompleted(question, givenAnswer, result, responseTime);

        totalResponseTime += responseTime;
        averageResponseTime = totalResponseTime / totalNumberOfQuestions;

        switch (result) {
            case CORRECT:
                correctQuestions.add(questionCompleted);
                break;
            case WRONG:
                wrongQuestions.add(questionCompleted);
                break;
        }
        return questionCompleted;
    }
}
