package com.matteoveroni.wordsremember.user_profile;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.settings.model.Settings;

import javax.inject.Inject;

public class UserProfilePresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Override
    public UserProfilePresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new UserProfilePresenter(settings);
    }
}
