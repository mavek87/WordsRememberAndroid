package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementViewLayoutManager;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

public class DictionaryManagementPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;
    @Inject
    DictionaryManagementViewLayoutManager injectedLayoutManager;

    @Override
    public DictionaryManagementActivityPresenter create() {
        MyApp.getModelComponent().inject(this);
        return new DictionaryManagementActivityPresenter(injectedModel, injectedLayoutManager);
    }
}
