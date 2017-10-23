package com.matteoveroni.wordsremember.user_profile;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 *
 * @author Matteo Veroni
 *
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class UserProfilePresenter implements Presenter {

    private UserProfileView view;

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    void onProfileSelected() {
        view.switchToView(View.Name.MAIN_MENU);
    }

}
