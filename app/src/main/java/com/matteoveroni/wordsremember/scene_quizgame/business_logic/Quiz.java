package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final QuizQuestionsChecker quizQuestionsChecker = new QuizQuestionsChecker();

    private Map<Integer, Question> questions = new HashMap<>();
    private int questionsIndex;
    private int totalNumberOfQuestions;

    public Quiz() {
        this.questionsIndex = -1;
    }

    public Quiz(int totalNumberOfQuestions) {
        this();
        this.totalNumberOfQuestions = totalNumberOfQuestions;
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

    public void addQuestion(Question question) {
        questionsIndex++;
        questions.put(questionsIndex, question);
    }

    public void addCorrectAnswerForCurrentQuestion(String answer) {
        if (!answer.trim().isEmpty()) {
            questions.get(questionsIndex).addCorrectAnswer(answer);
        }
    }

    public void answerCurrentQuestion(String givenAnswer) {
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.answer(givenAnswer);
        quizQuestionsChecker.checkAnswerResultForQuestion(givenAnswer, currentQuestion);
    }

    public void forceQuestionAnswerResult(QuestionAnswerResult result) {
        quizQuestionsChecker.forceAnswerResultForQuestion(result, getCurrentQuestion());
    }

    public int getNumberOfCorrectQuestions() {
        return quizQuestionsChecker.getNumberOfCorrectQuestions();
    }

    public int getNumberOfWrongQuestions() {
        return quizQuestionsChecker.getNumberOfWrongQuestions();
    }
}
