package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.dictionary.DictionaryManipulationActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;

import javax.inject.Inject;

public class DictionaryManipulationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryManipulationActivityPresenter create() {
        MyApp.getModelComponent().inject(this);
        return new DictionaryManipulationActivityPresenter(injectedModel);
    }
}
