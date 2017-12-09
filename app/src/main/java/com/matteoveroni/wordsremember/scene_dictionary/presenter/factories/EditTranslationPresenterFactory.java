package com.matteoveroni.wordsremember.scene_dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.EditTranslationPresenter;

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
