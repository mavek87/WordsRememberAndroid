package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;

    private DictionaryModel model = WordsRemember.DICTIONARY_MODEL;

    @Override
    public AddTranslationPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new AddTranslationPresenter(model, dao);
    }
}
