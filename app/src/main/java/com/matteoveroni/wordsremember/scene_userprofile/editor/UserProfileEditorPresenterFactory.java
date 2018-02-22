package com.matteoveroni.wordsremember.scene_userprofile.editor;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

public class UserProfileEditorPresenterFactory implements PresenterFactory {
    @Inject
    UserProfilesDAO dao;

    @Inject
    Settings settings;

    @Inject
    DBManager dbManager;

    @Override
    public UserProfileEditorPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new UserProfileEditorPresenter(dao, settings, dbManager);
    }
}
