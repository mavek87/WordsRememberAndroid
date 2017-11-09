package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;
import com.matteoveroni.wordsremember.scene_userprofile.editor.view.UserProfileEditorView;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter implements Presenter {
    private final UserProfileModel model;
    private UserProfileEditorView view;

    private static boolean VIEW_ATTACHED_FOR_THE_FIRST_TIME = true;

    public UserProfileEditorPresenter(UserProfileModel model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileEditorView) view;
        if(VIEW_ATTACHED_FOR_THE_FIRST_TIME) {
            UserProfile userProfile = model.getUserProfile();
            this.view.setPojoUsed(userProfile);
            VIEW_ATTACHED_FOR_THE_FIRST_TIME = false;
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onSaveProfileAction(UserProfile userProfile) {
        if (Str.isNullOrEmpty(userProfile.getProfileName())) {
            // TODO: use a formatted string
            view.showMessage("User profile name can\'t be empty. Type a valid name");
        } else {
            model.setUserProfile(userProfile);
            view.returnToPreviousView();
        }
    }
}
