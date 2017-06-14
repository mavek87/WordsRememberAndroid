package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
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

    private DictionaryModel model = WordsRemember.DICTIONARY_MODEL;

    @Override
    public EditTranslationPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new EditTranslationPresenter(model, dao);
    }
}
