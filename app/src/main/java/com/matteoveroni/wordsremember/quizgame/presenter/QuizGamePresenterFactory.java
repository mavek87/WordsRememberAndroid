package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class QuizGamePresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;

    @Override
    public QuizGamePresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new QuizGamePresenter(dao);
    }
}
