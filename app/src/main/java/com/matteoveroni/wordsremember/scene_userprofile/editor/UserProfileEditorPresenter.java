package com.matteoveroni.wordsremember.scene_userprofile.editor;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_userprofile.management.UserProfileView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter implements Presenter {

    private UserProfileView view;
    private final EventBus EVENTBUS = EventBus.getDefault();

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

    public void onSaveProfileAction() {
//        view.switchToView(View.Name.USER_PROFILE_EDITOR);
    }
}
