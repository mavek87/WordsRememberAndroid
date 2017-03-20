package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.presenter.EditVocablePresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class EditVocablePresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;
    private final DictionaryModel model = WordsRemember.getDictionaryModel();

    @Override
    public EditVocablePresenter create() {
        WordsRemember.getDAOComponent().inject(this);
        return new EditVocablePresenter(model, dao);
    }
}
