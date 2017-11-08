package com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment.events;

import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

/**
 * @author Matteo Veroni
 */

public class EventUserProfileSelected {
    private final UserProfile userProfile;

    public EventUserProfileSelected(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
