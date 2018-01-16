package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question;

import lombok.Getter;

/**
 * @author Matteo Veroni
 */

public class QuestionCompleted extends Question {

    @Getter
    private final String answer;
    @Getter
    private final AnswerResult answerResult;
    @Getter
    private final long responseTime;

    public enum AnswerResult {
        CORRECT, WRONG;
    }

    public QuestionCompleted(Question question, String answer, AnswerResult answerResult, long responseTime) {
        super(question.getQuestionMsg(), question.getTrueAnswers());
        this.answer = answer;
        this.answerResult = answerResult;
        this.responseTime = responseTime;
    }

    @Override
    public void addTrueAnswer(String answer) {
        throw new UnsupportedOperationException();
    }
}
