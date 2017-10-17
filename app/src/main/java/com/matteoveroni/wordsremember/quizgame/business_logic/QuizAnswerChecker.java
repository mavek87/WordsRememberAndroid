package com.matteoveroni.wordsremember.quizgame.business_logic;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;

/**
 * @author Matteo Veroni
 */

public class QuizAnswerChecker {

    private static final int LEVENSHTEIN_DISTANCE_THREESHOLD = 1;
    private static final LevenshteinDistance CORRECTNESS_APPROXIMATOR = new LevenshteinDistance(LEVENSHTEIN_DISTANCE_THREESHOLD);

    public static final boolean isCorrect(String answer, Collection<String> rightAnswers) {
        answer = answer.toLowerCase();

        for (String rightAnswer : rightAnswers) {
            if (CORRECTNESS_APPROXIMATOR.apply(answer, rightAnswer.toLowerCase()) <= 1) {
                return true;
            }
        }
        return false;
    }
}
