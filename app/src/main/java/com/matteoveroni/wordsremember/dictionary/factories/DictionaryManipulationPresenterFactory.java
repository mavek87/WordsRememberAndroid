package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.IDictionaryManipulationPresenter;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public IDictionaryManipulationPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new DictionaryManipulationPresenter(injectedModel);
    }
}
