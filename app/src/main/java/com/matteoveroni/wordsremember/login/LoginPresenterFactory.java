package com.matteoveroni.wordsremember.login;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.main_menu.MainMenuPresenter;

class LoginPresenterFactory implements PresenterFactory {

    @Override
    public LoginPresenter create() {
        return new LoginPresenter();
    }
}
