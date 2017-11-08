package com.matteoveroni.wordsremember.scene_userprofile.manager.view;

import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events.EventUserProfileSelected;

/**
 * @author Matteo Veroni
 */

public interface UserProfileView extends View {
    void addUserProfileAction();

    void selectUserProfileAction(EventUserProfileSelected event);

    void editUserProfileAction(EventEditUserProfile event);

    //TODO: add removeUserProfileAction
}
