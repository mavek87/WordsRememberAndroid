package com.matteoveroni.wordsremember.users;

/**
 * @author Matteo Veroni
 */

public class NotPersistedUser extends User {

    public static long NOT_PERSISTED_USER_ID = -1L;

    public NotPersistedUser(String username, String email){
        super(NOT_PERSISTED_USER_ID, username, email);
    }
}
