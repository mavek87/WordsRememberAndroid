package com.matteoveroni.wordsremember.scene_userprofile.manager.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.UserProfileView;

/**
 * @author Matteo Veroni
 */

public class UserProfilePresenter implements Presenter {

    protected static final int USER_PROFILE_EDITOR_REQUEST_CODE = 0;

    private UserProfileView view;
    private final Settings settings;
    private final UserProfileModel model;
    private final UserProfilesDAO dao;

    public UserProfilePresenter(Settings settings, UserProfileModel model, UserProfilesDAO dao) {
        this.settings = settings;
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onUserProfileSelectedAction(UserProfile selectedUserProfile) {
        settings.setUserProfile(selectedUserProfile);
        view.switchToView(View.Name.MAIN_MENU);
    }

    public void onAddProfileAction() {
        model.setUserProfile(UserProfile.createEmptyProfile());
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    public void onEditProfileAction(UserProfile userProfileToEdit) {
        model.setUserProfile(userProfileToEdit);
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    public void onDeleteProfileAction(UserProfile userProfileToRemove) {
        dao.deleteUserProfile(userProfileToRemove);
    }
}
