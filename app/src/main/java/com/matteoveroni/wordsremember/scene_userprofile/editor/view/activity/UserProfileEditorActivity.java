package com.matteoveroni.wordsremember.scene_userprofile.editor.view.activity;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventUserProfileSelected;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenter;
import com.matteoveroni.wordsremember.scene_userprofile.manager.presenter.UserProfilePresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileEditorActivity extends BaseActivityPresentedView implements UserProfileView {

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

    @Subscribe
    @Override
    public void editUserProfileAction(EventEditUserProfile event) {
        UserProfile userProfileToedit = event.getUserProfile();
        presenter.onEditProfileAction(userProfileToedit);
    }

    @Subscribe
    @Override
    public void selectUserProfileAction(EventUserProfileSelected event) {
        presenter.onUserProfileSelectedAction(event.getUserProfile());
    }
}

