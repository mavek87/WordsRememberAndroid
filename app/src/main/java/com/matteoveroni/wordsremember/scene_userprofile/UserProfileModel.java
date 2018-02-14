package com.matteoveroni.wordsremember.scene_userprofile;

/**
 * @author Matteo Veroni
 */

public class UserProfileModel {
    private Profile userProfile = Profile.createEmptyProfile();

    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile usedUserProfile) {
        this.userProfile = usedUserProfile;
    }
}
