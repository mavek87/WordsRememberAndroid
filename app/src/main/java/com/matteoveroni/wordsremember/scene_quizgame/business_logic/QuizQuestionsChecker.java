package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * @author Matteo Veroni
 */

public class QuizQuestionsChecker {

    private static final int LEVENSHTEIN_DISTANCE_THREESHOLD = 1;
    private static final LevenshteinDistance CORRECTNESS_APPROXIMATOR = new LevenshteinDistance(LEVENSHTEIN_DISTANCE_THREESHOLD);

    public QuestionCompleted.AnswerResult checkAnswerResultForQuestion(String answer, Question question) {
        for (String correctAnswer : question.getTrueAnswers()) {
            int correctness_value = CORRECTNESS_APPROXIMATOR.apply(answer, correctAnswer.toLowerCase());
            if (correctness_value >= 0) {
                return QuestionCompleted.AnswerResult.CORRECT;
            }
        }
        return QuestionCompleted.AnswerResult.WRONG;
    }
}
