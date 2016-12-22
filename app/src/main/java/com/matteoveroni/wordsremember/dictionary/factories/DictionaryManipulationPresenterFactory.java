package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

public class DictionaryManipulationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryManipulationPresenter create() {
        MyApp.getModelComponent().inject(this);
        return new DictionaryManipulationPresenter(injectedModel);
    }
}