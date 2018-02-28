package com.matteoveroni.wordsremember.scene_userprofile.manager;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.commands.CommandStoreAndSetAppUserProfile;
import com.matteoveroni.wordsremember.persistency.dao.UserProfilesDAO;
import com.matteoveroni.wordsremember.scene_settings.exceptions.NoRegisteredUserException;
import com.matteoveroni.wordsremember.scene_settings.exceptions.UnreadableUserInSettingsException;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventDeleteUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventRequestToEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventUserProfileChosen;
import com.matteoveroni.wordsremember.users.User;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class UserProfileManagerPresenter extends BasePresenter<UserProfileManagerView> {

    protected static final int USER_PROFILE_EDITOR_REQUEST_CODE = 0;

    private final Settings settings;
    private final DBManager dbManager;
    private final UserProfilesDAO dao;

    public UserProfileManagerPresenter(Settings settings, DBManager dbManager, UserProfilesDAO dao) {
        this.settings = settings;
        this.dbManager = dbManager;
        this.dao = dao;
    }

    public void onAddProfileAction() {
        EVENT_BUS.postSticky(new EventEditUserProfile(null));
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    @Subscribe
    public void onEvent(EventRequestToEditUserProfile event) {
        EVENT_BUS.postSticky(new EventEditUserProfile(event.getUserProfile()));
        view.switchToView(View.Name.USER_PROFILE_EDITOR, USER_PROFILE_EDITOR_REQUEST_CODE);
    }

    @Subscribe
    public void onEvent(EventDeleteUserProfile event) {
        try {
            dao.deleteUserProfile(event.getUserProfile());
        } catch (Exception ex) {
            // TODO: localize this message
            view.showMessage(ex.getMessage());
        }
    }

    @Subscribe
    public void onEvent(EventUserProfileChosen event) {
        //TODO: unify duplicated code in UserProfileEditorPresenter
        try {
            User currentUser = settings.getRegisteredUser();
            Profile chosenUserProfile = event.getUserProfile();
            if (chosenUserProfile.getUser() == null)
                chosenUserProfile.setUser(currentUser);

            new CommandStoreAndSetAppUserProfile(chosenUserProfile, settings, dbManager).execute();

        } catch (NoRegisteredUserException e) {
            throw new RuntimeException("Unexpected exception. No registered user in the prefs file!");
        } catch (UnreadableUserInSettingsException e) {
            throw new RuntimeException("Unexpected exception. UnreadableUserInSettingsException in the prefs file!");
        }

        view.finish();
        view.switchToView(View.Name.MAIN_MENU);
    }
}
