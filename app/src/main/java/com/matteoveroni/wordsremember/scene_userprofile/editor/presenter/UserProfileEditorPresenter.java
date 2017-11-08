package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter implements Presenter {
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private UserProfileView view;

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileView) view;
        EVENT_BUS.register(this);
    }

    @Override
    public void detachView() {
        EVENT_BUS.unregister(this);
        this.view = null;
    }

    public void onSaveProfileAction() {
//        view.switchToView(View.Name.USER_PROFILE_EDITOR);
    }
}
