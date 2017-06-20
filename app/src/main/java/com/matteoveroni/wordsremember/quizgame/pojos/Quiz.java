package com.matteoveroni.wordsremember.quizgame.pojos;

import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final int quizNumber;
    private final int totalNumberOfQuizzes;
    private final String question;
    private final List<String> rightAnswers;
    private String finalAnswer;
    private FinalResult finalResult;

    public enum FinalResult {
        CORRECT, WRONG;
    }

    public Quiz(int quizNumber, int totalNumberOfQuizzes, String question, List<String> rightAnswers) {
        if (rightAnswers.isEmpty())
            throw new IllegalArgumentException("Trying to create a quiz without setting right answers");

        this.quizNumber = quizNumber;
        this.totalNumberOfQuizzes = totalNumberOfQuizzes;
        this.question = question;
        this.rightAnswers = rightAnswers;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public int getTotalNumberOfQuizzes() {
        return totalNumberOfQuizzes;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getRightAnswers() {
        return rightAnswers;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public FinalResult getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(FinalResult finalResult) {
        this.finalResult = finalResult;
    }
}
