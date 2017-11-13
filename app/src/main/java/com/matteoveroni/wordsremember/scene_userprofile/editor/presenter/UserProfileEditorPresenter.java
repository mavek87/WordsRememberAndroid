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
        final UserProfile view_userProfile = view.getPojoUsed();
        if (view_userProfile.getName().trim().isEmpty()) {
            // TODO: use a formatted string
            view.showMessage("User profile name can\'t be empty, type a valid name");
            return;
        }

        final UserProfile model_userProfile = model.getUserProfile();
        if (model_userProfile.getId() <= 0) {
            dao.saveUserProfile(view_userProfile);
        } else {
            dao.updateUserProfile(model_userProfile, view_userProfile);
        }
        view.returnToPreviousView();
    }
}
