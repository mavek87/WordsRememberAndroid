package com.matteoveroni.wordsremember.main_menu;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.NullWeakReferenceProxy;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuPresenter implements Presenter{

    private MainMenuView view;

    @Override
    public void onViewAttached(Object view) {
        this.view = (MainMenuView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{MainMenuView.class},
                new NullWeakReferenceProxy(view)
        );
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    void onButtonManageDictionaryClicked() {
        view.startDictionaryManagement();
    }
}
