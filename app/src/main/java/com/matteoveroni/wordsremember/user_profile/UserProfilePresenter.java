package com.matteoveroni.wordsremember.user_profile;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
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

    public void onProfileSelected() {
        view.switchToView(View.Name.MAIN_MENU);
    }

}
