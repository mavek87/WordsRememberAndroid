package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.ManageVocablesPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class ManageVocablesPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedDAO;

    @Override
    public ManageVocablesPresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new ManageVocablesPresenter(injectedDAO);
    }
}
