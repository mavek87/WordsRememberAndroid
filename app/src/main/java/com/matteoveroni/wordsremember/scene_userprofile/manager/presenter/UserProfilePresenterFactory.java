package com.matteoveroni.wordsremember.scene_userprofile.manager.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;

import javax.inject.Inject;

public class UserProfilePresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Inject
    UserProfileModel model;

    @Override
    public UserProfilePresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new UserProfilePresenter(settings, model);
    }
}
