package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementViewLayoutManager;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

public class DictionaryManagementPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;
    @Inject
    DictionaryManagementViewLayoutManager injectedLayoutManager;

    @Override
    public DictionaryManagementPresenter create() {
        MyApp.getModelComponent().inject(this);
        return new DictionaryManagementPresenter(injectedModel, injectedLayoutManager);
    }
}
