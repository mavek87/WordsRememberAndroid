package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;
import com.matteoveroni.wordsremember.scene_userprofile.editor.view.UserProfileEditorView;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter implements Presenter {
    private boolean isPresenterCreatedForTheFirstTime;

    private final UserProfileModel model;
    private final UserProfilesDAO dao;

    private UserProfileEditorView view;

    public UserProfileEditorPresenter(UserProfileModel model, UserProfilesDAO dao) {
        this.model = model;
        this.dao = dao;
        isPresenterCreatedForTheFirstTime = true;
    }

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileEditorView) view;
        if (isPresenterCreatedForTheFirstTime) {
            this.view.setPojoUsed(model.getUserProfile());
            isPresenterCreatedForTheFirstTime = false;
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onSaveProfileAction() {
        final UserProfile model_userProfile = model.getUserProfile();
        final UserProfile view_userProfile = view.getPojoUsed();
        if (view_userProfile.getName().trim().isEmpty()) {
            // TODO: use a formatted string
            view.showMessage("The User profile name can\'t be empty, type a valid name");
            return;
        }

        if (model_userProfile.getId() <= 0) {
            try {
                dao.saveUserProfile(view_userProfile);
            } catch (Exception ex) {
                showSavedUserMessageAndGoToPreviousView(view_userProfile);
            }
        } else {
            try {
                dao.updateUserProfile(model_userProfile, view_userProfile);
                showSavedUserMessageAndGoToPreviousView(view_userProfile);
            } catch (Exception ex) {
                view.showMessage(ex.getMessage());
            }
        }
    }

    private void showSavedUserMessageAndGoToPreviousView(UserProfile userProfile) {
        view.showMessage("User profile " + userProfile.getName() + " saved!");
        view.returnToPreviousView();
    }
}
