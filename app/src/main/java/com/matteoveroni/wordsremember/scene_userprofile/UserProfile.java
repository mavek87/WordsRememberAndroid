package com.matteoveroni.wordsremember.scene_userprofile;

/**
 * @author Matteo Veroni
 */

public class UserProfile {

    /**
     * System profile used in the beginning of the app lifecycle for handling system_profile.db which
     * contains all the user profiles (in the profiles table).
     */
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
