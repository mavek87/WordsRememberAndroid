package com.matteoveroni.wordsremember.dictionary.presenter.factories;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationEditorPresenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationEditorPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO injectedModel;

    @Override
    public DictionaryTranslationEditorPresenter create() {
        WordsRemember.getModelComponent().inject(this);
        return new DictionaryTranslationEditorPresenter(injectedModel);
    }
}
