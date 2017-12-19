package com.matteoveroni.wordsremember.scene_quizgame.business_logic;

import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * @author Matteo Veroni
 */

public class QuizQuestionsChecker {

    private int numberOfCorrectQuestions;
    private int numberOfWrongQuestions;

    private static final int LEVENSHTEIN_DISTANCE_THREESHOLD = 1;
    private static final LevenshteinDistance CORRECTNESS_APPROXIMATOR = new LevenshteinDistance(LEVENSHTEIN_DISTANCE_THREESHOLD);

    public boolean checkAnswerResultForQuestion(String answer, Question question) {
        for (String correctAnswer : question.getCorrectAnswers()) {
            int correctness_value = CORRECTNESS_APPROXIMATOR.apply(answer, correctAnswer.toLowerCase());
            if (correctness_value >= 0) {
                setCorrect(question);
                return true;
            }
        }
        setWrong(question);
        return false;
    }

    public void forceAnswerResultForQuestion(QuestionAnswerResult result, Question question) {
        switch (result) {
            case CORRECT:
                setCorrect(question);
                break;
            case WRONG:
                setWrong(question);
                break;
            case NOT_ANSWERED_YET:
                question.setQuestionAnswerResult(result);
                break;
        }
    }

    public int getNumberOfCorrectQuestions() {
        return numberOfCorrectQuestions;
    }

    public int getNumberOfWrongQuestions() {
        return numberOfWrongQuestions;
    }

    public void reset() {
        numberOfCorrectQuestions = 0;
        numberOfWrongQuestions = 0;
    }

    private void setCorrect(Question question) {
        question.setQuestionAnswerResult(QuestionAnswerResult.CORRECT);
        numberOfCorrectQuestions++;
    }

    private void setWrong(Question question) {
        question.setQuestionAnswerResult(QuestionAnswerResult.WRONG);
        numberOfWrongQuestions++;
    }

}
