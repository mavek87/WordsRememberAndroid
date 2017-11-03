package com.matteoveroni.wordsremember.scene_login;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

public class LoginPresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Override
    public LoginPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new LoginPresenter(settings);
    }
}