package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationsManipulationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryTranslationsManipulationPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new DictionaryTranslationsManipulationPresenter(injectedModel);
    }
}
