package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.App;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryManipulationPresenter create() {
        App.getModelComponent().inject(this);
        return new DictionaryManipulationPresenter(injectedModel);
    }
}
