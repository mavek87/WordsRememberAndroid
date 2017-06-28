package com.matteoveroni.wordsremember.quizgame.pojos;

import java.util.List;
import java.util.Set;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final int quizNumber;
    private final int totalNumberOfQuizzes;
    private final String question;
    private volatile Set<String> rightAnswers;
    private String finalAnswer;
    private FinalResult finalResult;

    public enum FinalResult {
        CORRECT, WRONG;
    }

    public Quiz(int quizNumber, int totalNumberOfQuizzes, String question, Set<String> rightAnswers) {
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

    public synchronized Set<String> getRightAnswers() {
        return rightAnswers;
    }

    public synchronized void addRightAnswer(String answer) {
        rightAnswers.add(answer);
    }

    public synchronized void addRightAnswers(Set<String> newRightAnswers){
        this.rightAnswers.addAll(newRightAnswers);
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
