package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.EditTranslationPresenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;
    @Inject
    DictionaryModel model;

    @Override
    public EditTranslationPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new EditTranslationPresenter(model, dao);
    }
}
