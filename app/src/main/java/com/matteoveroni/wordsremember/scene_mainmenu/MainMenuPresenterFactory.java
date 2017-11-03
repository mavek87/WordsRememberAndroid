package com.matteoveroni.wordsremember.scene_mainmenu;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

class MainMenuPresenterFactory implements PresenterFactory {

    @Override
    public MainMenuPresenter create() {
        return new MainMenuPresenter();
    }
}
