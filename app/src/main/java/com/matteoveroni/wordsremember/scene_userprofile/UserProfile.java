package com.matteoveroni.wordsremember.scene_userprofile;

import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class UserProfile {

    /**
     * System profile used in the beginning of the app lifecycle for handling system_profile.db which
     * contains all the user profiles (in the profiles table).
     */
    public static final UserProfile SYSTEM_PROFILE = new UserProfile(1, "system_profile");

    private long id = -1;
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

    public boolean hasNullOrEmptyName() {
        return profileName == null || profileName.trim().isEmpty();
    }

    public static UserProfile fromJson(String json) {
        return Json.getInstance().fromJson(json, UserProfile.class);
    }

    public String toJson() {
        return Json.getInstance().toJson(this);
    }
}
