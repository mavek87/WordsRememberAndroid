package com.matteoveroni.wordsremember.main_menu;

import com.matteoveroni.wordsremember.presenters.Presenter;

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuPresenter implements Presenter {

    private MainMenuView view;

    @Override
    public void attachView(Object view) {
        this.view = (MainMenuView) view;
    }

    @Override
    public void destroy() {
        this.view = null;
    }

    void onButtonManageDictionaryClicked() {
        view.startDictionaryManagement();
    }
}
