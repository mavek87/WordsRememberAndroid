package com.matteoveroni.wordsremember.quizgame;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.quizgame.exceptions.NoMoreQuestionsException;

/**
 * @author Matteo Veroni
 */

public class QuizGameModel {

    private int remainingNumberOfQuestions;
    private DictionaryDAO dao;

    public QuizGameModel(int numberOfQuestions, DictionaryDAO dao) {
        if (numberOfQuestions < 0)
            throw new IllegalArgumentException("Illegal argument passed");

        this.dao = dao;

        
        this.remainingNumberOfQuestions = numberOfQuestions;
    }

    public int getRemainingNumberOfQuestions() {
        return remainingNumberOfQuestions;
    }

    public VocableTranslation getNextQuestion() throws NoMoreQuestionsException {
        if (getRemainingNumberOfQuestions() < 1) {
            throw new NoMoreQuestionsException();
        }
        remainingNumberOfQuestions--;
        return new VocableTranslation(new Word(), new Word());
    }
}
