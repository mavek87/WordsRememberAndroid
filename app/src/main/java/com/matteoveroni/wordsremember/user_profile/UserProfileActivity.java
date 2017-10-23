package com.matteoveroni.wordsremember.user_profile;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.main_menu.MainMenuView;

import butterknife.ButterKnife;

/**
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileActivity extends BaseActivityPresentedView implements UserProfileView {

    private UserProfilePresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new UserProfilePresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (UserProfilePresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_management);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.user_profile));
    }
}

