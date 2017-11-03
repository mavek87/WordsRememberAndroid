package com.matteoveroni.wordsremember.user_profile;

/**
 * @author Matteo Veroni
 */

public class UserProfile {

    public static final UserProfile SYSTEM_PROFILE = new UserProfile(1, "system_profile");

    private final long id;
    private final String profileName;

    public UserProfile(long id, String profileName) {
        this.id = id;
        this.profileName = profileName;
    }

    public long getId() {
        return id;
    }

    public String getProfileName() {
        return profileName;
    }
}
