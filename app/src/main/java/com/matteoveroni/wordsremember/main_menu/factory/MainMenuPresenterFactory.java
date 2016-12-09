package com.matteoveroni.wordsremember.main_menu.factory;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.main_menu.MainMenuConcretePresenter;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuPresenter;

public class MainMenuPresenterFactory implements PresenterFactory {
    @Override
    public MainMenuPresenter create() {
        return new MainMenuConcretePresenter();
    }
}
