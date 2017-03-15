package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.EditTranslationPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public EditTranslationPresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new EditTranslationPresenter(injectedModel);
    }
}
