package com.matteoveroni.wordsremember.quizgame.presenter;

import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class QuizGamePresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;
    @Inject
    Settings settings;

    @Override
    public QuizGamePresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new QuizGamePresenter(settings, dao);
    }
}
