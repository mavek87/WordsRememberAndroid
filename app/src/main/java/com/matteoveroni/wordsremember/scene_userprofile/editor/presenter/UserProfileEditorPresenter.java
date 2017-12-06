package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
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
    private final Settings settings;

    private UserProfileEditorView view;

    public UserProfileEditorPresenter(UserProfileModel model, UserProfilesDAO dao, Settings settings) {
        this.model = model;
        this.dao = dao;
        this.settings = settings;
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
        final UserProfile modelUserProfile = model.getUserProfile();
        final UserProfile viewUserProfile = view.getPojoUsed();
        if (viewUserProfile.getName().trim().isEmpty()) {
            // TODO: use storeViewUserProfileInTheModel formatted string
            view.showMessage("The User profile name can\'t be empty, type storeViewUserProfileInTheModel valid name");
            return;
        }

        try {
            storeViewUserProfileInTheModel(modelUserProfile, viewUserProfile);
            if (settings.isAppStartedForTheFirstTime()) {
                settings.setUserProfile(viewUserProfile);
                settings.setAppStartedForTheFirstTime(false);
                view.finish();
                view.switchToView(View.Name.MAIN_MENU);
            } else {
                view.showMessage("User profile " + viewUserProfile.getName() + " saved!");
                view.returnToPreviousView();
            }
        } catch (Exception ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void storeViewUserProfileInTheModel(UserProfile modelUserProfile, UserProfile viewUserProfile) throws Exception {
        if (modelUserProfile.getId() <= 0) {
            dao.saveUserProfile(viewUserProfile);
        } else {
            dao.updateUserProfile(modelUserProfile, viewUserProfile);
        }
    }
}
