package com.matteoveroni.wordsremember.scene_userprofile.management;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class UserProfilePresenter implements Presenter {

    private UserProfileView view;
    private final Settings settings;
    private final EventBus EVENTBUS = EventBus.getDefault();

    public UserProfilePresenter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileView) view;
        EVENTBUS.register(this);
    }

    @Override
    public void detachView() {
        EVENTBUS.unregister(this);
        this.view = null;
    }

    @Subscribe
    public void onProfileSelected(EventUserProfileSelected event) {
        settings.setUserProfile(event.getUserProfile());
        view.switchToView(View.Name.MAIN_MENU);
    }

    public void onAddProfileAction() {
        view.switchToView(View.Name.EDIT_USER_PROFILE);
    }
}
