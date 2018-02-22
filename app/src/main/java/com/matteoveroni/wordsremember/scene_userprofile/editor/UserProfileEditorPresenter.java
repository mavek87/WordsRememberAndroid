package com.matteoveroni.wordsremember.scene_userprofile.editor;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.commands.CommandStoreAndSetAppUser;
import com.matteoveroni.wordsremember.persistency.commands.CommandStoreAndSetAppUserProfile;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.EmptyProfile;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.editor.interfaces.UserProfileEditorExtendedView;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.users.User;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorPresenter extends BasePresenter<UserProfileEditorExtendedView> {

    private final DBManager dbManager;
    private final UserProfilesDAO dao;
    private final Settings settings;
    private Profile userProfile;

    public UserProfileEditorPresenter(UserProfilesDAO dao, Settings settings, DBManager dbManager) {
        this.dbManager = dbManager;
        this.dao = dao;
        this.settings = settings;
    }

    @Subscribe(sticky = true)
    public void onEvent(EventEditUserProfile event) {
        userProfile = event.getUserProfile();
        if (userProfile instanceof EmptyProfile) {
            view.setHeader("Create");
        } else {
            view.setHeader("Edit");
        }
        view.setProfileName(userProfile.getName());
    }

    public void onSaveProfileAction() {
        try {
            saveUserProfileEditedFromView();

//            if (settings.isAppStartedForTheFirstTime()) {
//                new CommandSetSelectedUserProfile(userProfile, settings, dbManager).execute();
//                settings.setAppStartedForTheFirstTime(false);
//                view.finish();
//                view.switchToView(View.Name.MAIN_MENU);
//            } else {
//                view.showMessage("User profile " + view.getProfileName() + " saved!");
//                view.returnToPreviousView();
//            }

        } catch (Exception ex) {
            // Error writing to the db
            view.showMessage(ex.getMessage());
        }
    }

//    public void onSaveProfileAction() {
//        final Profile viewUserProfile = getUserProfileFromView();
//        if (isProfileValid(viewUserProfile)) {
//            try {
//                //TODO: handle the case of dictionary name already used in the db (correct error must be shown in the view!)
//                saveUserProfileNameFromView(viewUserProfile);
//
//                if (settings.isAppStartedForTheFirstTime()) {
//                    settings.saveUserProfile(userProfile);
//                    settings.setAppStartedForTheFirstTime(false);
//                    view.finish();
//                    view.switchToView(View.Name.MAIN_MENU);
//                } else {
//                    view.showMessage("User profile " + viewUserProfile.getName() + " saved!");
//                    view.returnToPreviousView();
//                }
//            } catch (Exception ex) {
//                view.showMessage(ex.getMessage());
//            }
//        } else {
//            view.showMessage(AndroidLocaleKey.MSG_ERROR_EMPTY_USER_PROFILE_NAME);
//        }
//    }

    private void saveUserProfileEditedFromView() throws Exception {
//        if (userProfile == null) {
//            final long id = dao.saveUserProfile(new Profile(-1L, userProfileName));
//            userProfile = new Profile(id, userProfileName);
//        } else {
//            final Profile viewUserProfile = getUserProfileFromView();
//            dao.updateUserProfile(userProfile, viewUserProfile);
//            userProfile = new Profile(userProfile.getId(), viewUserProfile.getName());
//        }
        Profile profileEditedFromView = new Profile(userProfile.getId(), view.getProfileName());
        if (profileEditedFromView.getId() > 0) {
            dao.updateUserProfile(userProfile, profileEditedFromView);
            userProfile = profileEditedFromView;
        } else {
            long id = dao.saveUserProfile(profileEditedFromView);
            userProfile = new Profile(id, view.getProfileName());
        }

        //TODO: unify duplicated code in UserProfileManagerPresenter
        try {
            User currentUser = settings.getUser();
            if (userProfile.getUser() == null)
                userProfile.setUser(currentUser);

            new CommandStoreAndSetAppUserProfile(userProfile, settings, dbManager).execute();

        } catch (Settings.NoRegisteredUserException e) {
            throw new RuntimeException("Unexpected exception. No registered user in the prefs file!");
        }
    }

//    private Profile getUserProfileFromView() {
//        return userProfile == null
//                ? null
//                : new Profile(userProfile.getId(), view.getProfileName());
//    }

//    private boolean isProfileValid(Profile profile) {
//        return profile != null && !profile.getName().trim().isEmpty();
//    }
}
