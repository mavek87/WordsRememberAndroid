package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import com.matteoveroni.androidtaggenerator.TagGenerator;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;

/**
 * @author Matteo Veroni
 */

public class QuizAnswerChecker {

    private static final String TAG = TagGenerator.tag(QuizAnswerChecker.class);

    private static final int LEVENSHTEIN_DISTANCE_THREESHOLD = 1;
    private static final LevenshteinDistance CORRECTNESS_APPROXIMATOR = new LevenshteinDistance(LEVENSHTEIN_DISTANCE_THREESHOLD);

    private String rightAnswer;

    public void reset() {
        rightAnswer = null;
    }

    public boolean isCorrect(String answer, Collection<String> rightAnswers) {
        answer = answer.toLowerCase();

        for (String rightAnswer : rightAnswers) {
            int correctness_value = CORRECTNESS_APPROXIMATOR.apply(answer, rightAnswer.toLowerCase());
            if (correctness_value >= 0) {
                this.rightAnswer = rightAnswer;
                return true;
            }
        }
        return false;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }
}
