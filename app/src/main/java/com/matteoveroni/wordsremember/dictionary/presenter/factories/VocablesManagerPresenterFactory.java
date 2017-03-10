package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.dictionary.presenter.VocablesManagerPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class VocablesManagerPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public VocablesManagerPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new VocablesManagerPresenter(injectedModel);
    }
}
