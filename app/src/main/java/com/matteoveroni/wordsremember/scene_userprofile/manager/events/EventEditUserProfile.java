package com.matteoveroni.wordsremember.scene_userprofile.manager.events;

import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class EventEditUserProfile {
    private final Profile userProfile;

    public EventEditUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public Profile getUserProfile() {
        return userProfile;
    }
}
