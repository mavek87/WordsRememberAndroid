package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

public class UserProfileEditorPresenterFactory implements PresenterFactory {

    @Override
    public UserProfileEditorPresenter create() {
        return new UserProfileEditorPresenter();
    }
}
