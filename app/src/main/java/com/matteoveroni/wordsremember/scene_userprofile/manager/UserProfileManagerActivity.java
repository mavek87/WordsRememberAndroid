package com.matteoveroni.wordsremember.scene_userprofile.manager;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileManagerActivity extends AbstractPresentedActivityView implements UserProfileManagerView {

    private UserProfileManagerPresenter presenter;

    @OnClick(R.id.add_profile_floating_action_button)
    @Override
    public void addUserProfileAction() {
        presenter.onAddProfileAction();
    }

//    @Override
//    @Subscribe
//    public void onEvent(EventUserProfileChosen event) {
//        presenter.onUserProfileSelectedAction(event.getUserProfile());
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(EventRequestToEditUserProfile event) {
//        presenter.onRequestToEditUserProfileAction(event.getUserProfile());
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(EventDeleteUserProfile event) {
//        presenter.onDeleteProfileAction(event.getUserProfile());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles_management);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.user_profile));
    }

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.USER_PROFILE_PRESENTER);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (UserProfileManagerPresenter) presenter;
    }
}

