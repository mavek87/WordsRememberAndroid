package com.matteoveroni.wordsremember.quizgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class Quiz {

    private final String question;
    private final List<String> rightAnswers;

    public Quiz(String question, List<String> rightAnswers) {
        if (rightAnswers.isEmpty())
            throw new IllegalArgumentException("Trying to create a quiz without setting right answers");

        this.question = question;
        this.rightAnswers = rightAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getRightAnswers() {
        return rightAnswers;
    }
}
