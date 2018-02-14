package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.AndroidLocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfileModel;
import com.matteoveroni.wordsremember.scene_userprofile.editor.view.UserProfileEditorView;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter extends BasePresenter<UserProfileEditorView> {
    private boolean isPresenterCreatedForTheFirstTime;

    private final UserProfileModel model;
    private final UserProfilesDAO dao;
    private final Settings settings;

    public UserProfileEditorPresenter(UserProfileModel model, UserProfilesDAO dao, Settings settings) {
        this.model = model;
        this.dao = dao;
        this.settings = settings;
        isPresenterCreatedForTheFirstTime = true;
    }

    @Override
    public void attachView(UserProfileEditorView view) {
        super.attachView(view);
        if (isPresenterCreatedForTheFirstTime) {
            this.view.setPojoUsed(model.getUserProfile());
            isPresenterCreatedForTheFirstTime = false;
        }
    }

    public void onSaveProfileAction() {
        final Profile viewUserProfile = view.getPojoUsed();
        if (viewUserProfile.getName().trim().isEmpty()) {
            view.showMessage(AndroidLocaleKey.MSG_ERROR_EMPTY_USER_PROFILE_NAME);
            return;
        }

        try {
            //TODO: handle the case of dictionary name already used in the db (correct error must be shown in the view!)
            storeUserProfileFromViewToModel(viewUserProfile);

            if (settings.isAppStartedForTheFirstTime()) {
                settings.setUserProfile(model.getUserProfile());
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

    private void storeUserProfileFromViewToModel(Profile viewUserProfile) throws Exception {
        final Profile modelUserProfile = model.getUserProfile();

        if (modelUserProfile.getId() <= 0) {
            final long id = dao.saveUserProfile(viewUserProfile);
            model.setUserProfile(new Profile(id, viewUserProfile.getName()));
        } else {
            dao.updateUserProfile(modelUserProfile, viewUserProfile);
            model.setUserProfile(modelUserProfile);
        }
    }
}
