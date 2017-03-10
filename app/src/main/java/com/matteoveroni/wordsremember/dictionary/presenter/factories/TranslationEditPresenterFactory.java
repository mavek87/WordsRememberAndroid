package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.TranslationEditPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class TranslationEditPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public TranslationEditPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new TranslationEditPresenter(injectedModel);
    }
}
