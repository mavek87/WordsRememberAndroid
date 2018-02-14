package com.matteoveroni.wordsremember.scene_userprofile.manager.events;

import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class EventUserProfileSelected {
    private final Profile userProfile;

    public EventUserProfileSelected(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public Profile getUserProfile() {
        return userProfile;
    }
}
