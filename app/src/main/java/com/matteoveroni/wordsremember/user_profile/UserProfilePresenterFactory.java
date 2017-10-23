package com.matteoveroni.wordsremember.user_profile;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

class UserProfilePresenterFactory implements PresenterFactory {

    @Override
    public UserProfilePresenter create() {
        return new UserProfilePresenter();
    }
}
