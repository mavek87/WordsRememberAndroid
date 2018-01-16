package com.matteoveroni.wordsremember.scene_userprofile.editor.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
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
        final UserProfile viewUserProfile = view.getPojoUsed();
        if (viewUserProfile.getName().trim().isEmpty()) {
            // TODO: is formatted string
            view.showMessage("The User profile name can\'t be empty, insert a valid name!");
            return;
        }

        try {
            //TODO: handle the case of dictionary name already used in the db (correct error must be shown in the view!)
            storeViewUserProfileInTheModel(viewUserProfile);

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

    private void storeViewUserProfileInTheModel(UserProfile viewUserProfile) throws Exception {
        final UserProfile modelUserProfile = model.getUserProfile();

        if (modelUserProfile.getId() <= 0) {
            final long id = dao.saveUserProfile(viewUserProfile);
            model.setUserProfile(new UserProfile(id, viewUserProfile.getName()));
        } else {
            dao.updateUserProfile(modelUserProfile, viewUserProfile);
            model.setUserProfile(modelUserProfile);
        }
    }
}
