package com.matteoveroni.wordsremember.main_menu.factory;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivityConcretePresenter;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuActivityPresenter;

public class MainMenuActivityPresenterFactory implements PresenterFactory {
    @Override
    public MainMenuActivityPresenter create() {
        return new MainMenuActivityConcretePresenter();
    }
}
