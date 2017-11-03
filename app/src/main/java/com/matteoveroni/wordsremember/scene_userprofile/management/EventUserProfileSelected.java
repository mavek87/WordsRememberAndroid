package com.matteoveroni.wordsremember.scene_userprofile.management;

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
