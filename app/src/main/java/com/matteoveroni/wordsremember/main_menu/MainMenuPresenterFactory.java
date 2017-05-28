package com.matteoveroni.wordsremember.main_menu;

import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.main_menu.MainMenuPresenter;

class MainMenuPresenterFactory implements PresenterFactory {

    @Override
    public MainMenuPresenter create() {
        return new MainMenuPresenter();
    }
}
