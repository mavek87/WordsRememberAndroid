package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;

import javax.inject.Inject;

public class UserProfileEditorPresenterFactory implements PresenterFactory {

    @Inject
    UserProfileModel model;

    @Override
    public UserProfileEditorPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new UserProfileEditorPresenter(model);
    }
}
