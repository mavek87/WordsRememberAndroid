package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;

    private DictionaryModel model = WordsRemember.getDictionaryModel();

    @Override
    public AddTranslationPresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new AddTranslationPresenter(model, dao);
    }
}
