package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import java.util.Set;

/**
 * @author Matteo Veroni
 */

public class Question {
    private final String questionMsg;
    private Set<String> correctAnswers;

    public Question(String questionMsg, Set<String> correctAnswers) {
        this.questionMsg = questionMsg;
        this.correctAnswers = correctAnswers;
    }

    public String getQuestionMsg() {
        return questionMsg;
    }

    public Set<String> getTrueAnswers() {
        return correctAnswers;
    }

    public void addTrueAnswer(String answer) {
        correctAnswers.add(answer);
    }
}
