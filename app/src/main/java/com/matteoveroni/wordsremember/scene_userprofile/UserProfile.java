package com.matteoveroni.wordsremember.scene_userprofile;

import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;

/**
 * @author Matteo Veroni
 */

public class UserProfile {

    /**
     * System profile (user profiles) is used in the initial lifecycle of the app for handling UserProfiles.db which
     * contains all the user profiles (profiles table).
     */
    public static final UserProfile USER_PROFILES = new UserProfile(0, UserProfilesContract.Schema.TABLE_NAME);

    public static final long NO_ID = -1;
    public static final String NO_PROFILE_NAME = "";

    private final Long id;
    private final String name;

    public UserProfile(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UserProfile createEmptyProfile() {
        return new UserProfile(NO_ID, NO_PROFILE_NAME);
    }

    public boolean isInvalidProfile() {
        return (id <= 0) || (name.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile that = (UserProfile) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
