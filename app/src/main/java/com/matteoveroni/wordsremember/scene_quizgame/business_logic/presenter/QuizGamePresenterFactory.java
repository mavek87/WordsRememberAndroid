package com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

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
