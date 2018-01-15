package com.matteoveroni.wordsremember.scene_mainmenu;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;

public class MainMenuPresenterFactory implements PresenterFactory {

    @Override
    public MainMenuPresenter create() {
        return new MainMenuPresenter();
    }
}
