package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.quizgame.QuizGameModel;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class QuizGamePresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;

    private QuizGameModel model = WordsRemember.QUIZ_GAME_MODEL;

    @Override
    public QuizGamePresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new QuizGamePresenter(model, dao);
    }
}
