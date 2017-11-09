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

    private long id;
    private String profileName;

    public UserProfile(String profileName) {
        this.profileName = profileName;
    }

    public UserProfile(long id, String profileName) {
        this(profileName);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
