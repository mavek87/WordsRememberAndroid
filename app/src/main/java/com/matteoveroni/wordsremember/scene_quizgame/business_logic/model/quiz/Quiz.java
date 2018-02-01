package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz;

import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.Question;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.QuestionAnswerChecker;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.CompletedQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final Map<Integer, Question> questions = new HashMap<>();
    private final List<CompletedQuestion> correctAnswers = new ArrayList<>();
    private final List<CompletedQuestion> wrongAnswers = new ArrayList<>();
    private final QuestionAnswerChecker questionAnswerChecker = new QuestionAnswerChecker();
    private final GameDifficulty gameDifficulty;

    private int questionsIndex;
    private int totalNumberOfQuestions;
    private long totalResponseTime;
    private long averageResponseTime;

    public Quiz(GameDifficulty gameDifficulty) {
        this.questionsIndex = -1;
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

    public List<CompletedQuestion> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<CompletedQuestion> getWrongAnswers() {
        return wrongAnswers;
    }

    public long getTotalResponseTime() {
        return totalResponseTime;
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void addQuestion(Question question) {
        questionsIndex++;
        questions.put(questionsIndex, question);
    }

    public void addTrueAnswerForCurrentQuestion(String answer) {
        if (!answer.trim().isEmpty()) {
            questions.get(questionsIndex).addTrueAnswer(answer);
        }
    }

    public CompletedQuestion answerCurrentQuestion(String givenAnswer, long responseTime) {
        return answerQuestion(getCurrentQuestion(), givenAnswer, responseTime);
    }

    public CompletedQuestion forceQuestionAnswerResult(CompletedQuestion.AnswerResult result, long responseTime) {
        return answerQuestion(getCurrentQuestion(), "", result, responseTime);
    }

    private CompletedQuestion answerQuestion(Question question, String givenAnswer, long responseTime) {
        final CompletedQuestion.AnswerResult result = questionAnswerChecker.checkAnswerResultForQuestion(givenAnswer, question);
        return answerQuestion(question, givenAnswer, result, responseTime);
    }

    private CompletedQuestion answerQuestion(Question question, String givenAnswer, CompletedQuestion.AnswerResult result, long responseTime) {
        CompletedQuestion completedQuestion = new CompletedQuestion(question, givenAnswer, result, responseTime);

        totalResponseTime += responseTime;
        averageResponseTime = totalResponseTime / totalNumberOfQuestions;

        switch (result) {
            case CORRECT:
                correctAnswers.add(completedQuestion);
                break;
            case WRONG:
                wrongAnswers.add(completedQuestion);
                break;
        }
        return completedQuestion;
    }
}
