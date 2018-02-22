package com.matteoveroni.wordsremember.scene_userprofile.manager;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

public class UserProfileManagerPresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Inject
    DBManager dbManager;

    @Inject
    UserProfilesDAO dao;

    @Override
    public UserProfileManagerPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new UserProfileManagerPresenter(settings, dbManager, dao);
    }
}
