package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question;

/**
 * @author Matteo Veroni
 */

public class QuestionCompleted extends Question {

    private final String answer;
    private final AnswerResult answerResult;
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

    public AnswerResult getAnswerResult() {
        return answerResult;
    }

    public String getAnswer() {
        return answer;
    }

    public long getResponseTime() {
        return responseTime;
    }

    @Override
    public void addTrueAnswer(String answer) {
        throw new UnsupportedOperationException();
    }
}
