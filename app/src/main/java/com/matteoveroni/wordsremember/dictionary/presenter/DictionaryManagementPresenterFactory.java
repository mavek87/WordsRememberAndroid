package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
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
        WordsRemember.getModelComponent().inject(this);
        return new DictionaryManagementPresenter(injectedModel);
    }
}
