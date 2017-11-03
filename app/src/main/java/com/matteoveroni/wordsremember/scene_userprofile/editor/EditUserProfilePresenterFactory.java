package com.matteoveroni.wordsremember.scene_userprofile.editor;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

public class EditUserProfilePresenterFactory implements PresenterFactory {

    @Override
    public EditUserProfilePresenter create() {
        return new EditUserProfilePresenter();
    }
}
