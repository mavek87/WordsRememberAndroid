package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * @author Matteo Veroni
 */

public class QuizAnswerChecker {

    private final Quiz quiz;

    public QuizAnswerChecker(Quiz quiz) {
        this.quiz = quiz;
    }

    public boolean isFinalAnswerCorrect() {
        String finalAnswer = quiz.getFinalAnswer();
        for (String rightAnswer : quiz.getRightAnswers()) {
            if (finalAnswer.equalsIgnoreCase(rightAnswer)) {
                return true;
            }
        }
        return false;
    }
}
