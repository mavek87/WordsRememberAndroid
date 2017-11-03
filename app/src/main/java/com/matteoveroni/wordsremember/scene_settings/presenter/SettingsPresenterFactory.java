package com.matteoveroni.wordsremember.scene_settings.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Override
    public SettingsPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new SettingsPresenter(settings);
    }
}
