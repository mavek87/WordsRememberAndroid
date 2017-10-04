package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.ManageVocablesPresenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class ManageVocablesPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;
    @Inject
    DictionaryModel model;

    @Override
    public ManageVocablesPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new ManageVocablesPresenter(model, dao);
    }
}
