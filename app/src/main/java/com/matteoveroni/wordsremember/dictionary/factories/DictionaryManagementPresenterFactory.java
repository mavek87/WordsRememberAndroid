package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryManagementPresenter create() {
        MyApp.getModelComponent().inject(this);
        return new DictionaryManagementPresenter(injectedModel);
    }
}
