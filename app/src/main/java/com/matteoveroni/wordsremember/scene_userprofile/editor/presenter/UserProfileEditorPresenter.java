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
    private static boolean FIRST_TIME_CREATED = true;

    private final UserProfileModel model;
    private final UserProfilesDAO dao;

    private UserProfileEditorView view;

    public UserProfileEditorPresenter(UserProfileModel model, UserProfilesDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (UserProfileEditorView) view;
        if (FIRST_TIME_CREATED) {
            this.view.setPojoUsed(model.getUserProfile());
            FIRST_TIME_CREATED = false;
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onSaveProfileAction() {
        final UserProfile editedUserProfile = view.getPojoUsed();
        if (editedUserProfile.hasNullOrEmptyName()) {
            // TODO: use a formatted string
            view.showMessage("User profile name can\'t be empty. Type a valid name");
            return;
        }

        final UserProfile persistingUserProfileToEdit = model.getUserProfile();
        if (persistingUserProfileToEdit.hasNullOrEmptyName() || persistingUserProfileToEdit.getId() <= 0) {
            dao.saveUserProfile(editedUserProfile);
        } else {
            dao.updateUserProfile(persistingUserProfileToEdit.getId(), editedUserProfile);
            editedUserProfile.setId(persistingUserProfileToEdit.getId());
        }
        model.setUserProfile(editedUserProfile);
        view.returnToPreviousView();
    }
}
