package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.TranslationEditPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.TranslationSelectorPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class TranslationSelectorPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public TranslationSelectorPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new TranslationSelectorPresenter(injectedModel);
    }
}
