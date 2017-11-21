package com.matteoveroni.wordsremember.scene_userprofile.manager.events;

import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

/**
 * @author Matteo Veroni
 */

public class EventEditUserProfile {
    private final UserProfile userProfile;

    public EventEditUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
