package com.matteoveroni.wordsremember.scene_userprofile;

import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
@EqualsAndHashCode
public class Profile {

    /**
     * System profile (user profiles) is used in the initial lifecycle of the app for handling UserProfiles.db which
     * contains all the user profiles (profiles table).
     */
    public static final Profile USER_PROFILES = new Profile(0L, UserProfilesContract.Schema.TABLE_NAME);

    public static final long NO_ID = -1;
    public static final String NO_PROFILE_NAME = "";

    @Getter
    private final Long id;
    @Getter
    private final String name;

    public static Profile createEmptyProfile() {
        return new Profile(NO_ID, NO_PROFILE_NAME);
    }

    public boolean isInvalidProfile() {
        return (id <= 0) || (name.trim().isEmpty());
    }
}