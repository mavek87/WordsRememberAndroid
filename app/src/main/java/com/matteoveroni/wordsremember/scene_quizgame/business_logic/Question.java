package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class Question {
    private final String questionMsg;
    private String givenAnswer;
    private Set<String> correctAnswers;
    private QuestionAnswerResult questionAnswerResult = QuestionAnswerResult.NOT_ANSWERED_YET;

    public Question(String questionMsg, Set<String> correctAnswers) {
        this.questionMsg = questionMsg;
        this.correctAnswers = correctAnswers;
    }

    public String getQuestionMsg() {
        return questionMsg;
    }

    public Set<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void addCorrectAnswer(String answer) {
        correctAnswers.add(answer);
    }

    public void setQuestionAnswerResult(QuestionAnswerResult questionAnswerResult) {
        this.questionAnswerResult = questionAnswerResult;
    }

    public boolean isQuestionAnswered() {
        return (questionAnswerResult != QuestionAnswerResult.NOT_ANSWERED_YET);
    }

    public QuestionAnswerResult getQuestionAnswerResult() {
        return questionAnswerResult;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public void answer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }
}
