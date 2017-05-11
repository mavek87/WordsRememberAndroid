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

    public enum Result {
        RIGHT, WRONG;
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
}
