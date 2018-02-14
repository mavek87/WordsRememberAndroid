package com.matteoveroni.wordsremember.scene_userprofile.manager.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

/**
 * @author Matteo Veroni
 */

public class UserProfilePresenter extends BasePresenter<UserProfileView> {

    protected static final int USER_PROFILE_EDITOR_REQUEST_CODE = 0;

    private final Settings settings;
    private final UserProfileModel model;
    private final UserProfilesDAO dao;

    public UserProfilePresenter(Settings settings, UserProfileModel model, UserProfilesDAO dao) {
        this.settings = settings;
        this.settings.setUserProfile(Profile.USER_PROFILES);
        this.model = model;
        this.dao = dao;
    }

    public void onUserProfileSelectedAction(Profile selectedUserProfile) {
        settings.setUserProfile(selectedUserProfile);
        view.finish();
        view.switchToView(View.Name.MAIN_MENU);
    }

    public void onAddProfileAction() {
        model.setUserProfile(Profile.createEmptyProfile());
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    public void onEditProfileAction(Profile userProfileToEdit) {
        model.setUserProfile(userProfileToEdit);
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    public void onDeleteProfileAction(Profile userProfileToRemove) {
        try {
            dao.deleteUserProfile(userProfileToRemove);
        } catch (Exception ex) {
            view.showMessage(ex.getMessage());
        }
    }
}
