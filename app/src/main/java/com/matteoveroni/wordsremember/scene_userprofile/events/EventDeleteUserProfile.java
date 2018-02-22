package com.matteoveroni.wordsremember.scene_userprofile.events;

import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class EventDeleteUserProfile {
    private final Profile userProfile;

    public EventDeleteUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public Profile getUserProfile() {
        return userProfile;
    }
}
