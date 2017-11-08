package com.matteoveroni.wordsremember.scene_userprofile.manager.view.activity;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenter;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventUserProfileSelected;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileActivity extends BaseActivityPresentedView implements UserProfileView {

    private static final EventBus EVENT_BUS = EventBus.getDefault();
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
    protected void onStart() {
        super.onStart();
        EVENT_BUS.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EVENT_BUS.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_management);
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
}

