package com.matteoveroni.wordsremember.scene_userprofile.manager.view.activity;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventDeleteUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventUserProfileSelected;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenter;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileActivity extends AbstractPresentedActivityView implements UserProfileView {

    private UserProfilePresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.USER_PROFILE_PRESENTER);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (UserProfilePresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles_management);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.user_profile));
    }

    @OnClick(R.id.add_profile_floating_action_button)
    @Override
    public void addUserProfileAction() {
        presenter.onAddProfileAction();
    }

    @Override
    @Subscribe
    public void selectUserProfileAction(EventUserProfileSelected event) {
        presenter.onUserProfileSelectedAction(event.getUserProfile());
    }

    @Override
    @Subscribe
    public void editUserProfileAction(EventEditUserProfile event) {
        presenter.onEditProfileAction(event.getUserProfile());
    }

    @Override
    @Subscribe
    public void deleteUserProfileAction(EventDeleteUserProfile event) {
        presenter.onDeleteProfileAction(event.getUserProfile());
    }
}

