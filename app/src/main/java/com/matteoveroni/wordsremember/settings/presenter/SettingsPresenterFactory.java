package com.matteoveroni.wordsremember.settings.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.settings.model.Settings;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenterFactory implements PresenterFactory {

    @Inject
    DictionaryDAO dao;
    @Inject
    Settings settings;

    @Override
    public SettingsPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new SettingsPresenter(settings, dao);
    }
}
