package com.matteoveroni.wordsremember.dictionary.management.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import javax.inject.Inject;

public class DictionaryManagementPresenterFactory implements PresenterFactory {

    @Override
    public DictionaryManagementPresenter create() {
        return new DictionaryManagementActivityPresenter();
    }
}
