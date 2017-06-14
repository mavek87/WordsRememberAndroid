package com.matteoveroni.wordsremember.main_menu;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

class MainMenuPresenterFactory implements PresenterFactory {

    @Override
    public MainMenuPresenter create() {
        return new MainMenuPresenter();
    }
}
