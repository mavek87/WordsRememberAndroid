package com.matteoveroni.wordsremember.quizgame.business_logic;

import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * @author Matteo Veroni
 */

public class QuizFinalAnswerChecker {

    public static final boolean isFinalAnswerCorrect(Quiz quiz) {
        String finalAnswer = quiz.getFinalAnswer();
        for (String rightAnswer : quiz.getRightAnswers()) {
            if (finalAnswer.equalsIgnoreCase(rightAnswer)) {
                return true;
            }
        }
        return false;
    }
}
