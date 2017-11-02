package com.matteoveroni.wordsremember.user_profile;

/**
 * @author Matteo Veroni
 */

public class UserProfile {

    public static final UserProfile SYSTEM_PROFILE = new UserProfile("system_profile");

    private final String profileName;

    public UserProfile(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
