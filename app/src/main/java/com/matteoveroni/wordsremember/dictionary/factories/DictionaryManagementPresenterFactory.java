package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;

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
