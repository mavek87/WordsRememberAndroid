package com.matteoveroni.wordsremember.scene_userprofile.manager.view;

import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventDeleteUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventUserProfileSelected;

/**
 * @author Matteo Veroni
 */

public interface UserProfileView extends View {
    void addUserProfileAction();

    void selectUserProfileAction(EventUserProfileSelected event);

    void editUserProfileAction(EventEditUserProfile event);

    void deleteUserProfileAction(EventDeleteUserProfile event);

    void finish();
}
