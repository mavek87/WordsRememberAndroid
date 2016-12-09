package com.matteoveroni.wordsremember.main_menu;

import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuActivityPresenter;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuActivityView;
import com.matteoveroni.wordsremember.models.NullObjectProxy;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuActivityConcretePresenter implements MainMenuActivityPresenter {

    private MainMenuActivityView view;

    @Override
    public void onViewAttached(Object view) {
        this.view = (MainMenuActivityView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{MainMenuActivityView.class},
                new NullObjectProxy(view)
        );
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {
        onViewDetached();
    }

    @Override
    public void onButtonManageDictionaryClicked() {
        view.startDictionaryManagement();
    }
}
