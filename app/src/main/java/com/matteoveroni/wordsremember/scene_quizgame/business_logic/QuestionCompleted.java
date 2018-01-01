package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

/**
 * @author Matteo Veroni
 */

public class QuestionCompleted extends Question {

    private final String givenAnswer;
    private final AnswerResult answerResult;

    public enum AnswerResult {
        CORRECT, WRONG;
    }

    public QuestionCompleted(Question question, String givenAnswer, AnswerResult answerResult) {
        super(question.getQuestionMsg(), question.getTrueAnswers());
        this.givenAnswer = givenAnswer;
        this.answerResult = answerResult;
    }

    public AnswerResult getAnswerResult() {
        return answerResult;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }
}
