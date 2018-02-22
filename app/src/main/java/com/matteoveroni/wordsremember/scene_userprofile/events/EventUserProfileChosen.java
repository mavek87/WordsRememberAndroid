package com.matteoveroni.wordsremember.scene_userprofile.events;

import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class EventUserProfileChosen {
    private final Profile userProfile;

    public EventUserProfileChosen(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public Profile getUserProfile() {
        return userProfile;
    }
}
