package com.matteoveroni.wordsremember.users;

/**
 * @author Matteo Veroni
 */

public class User {
    private long id;

    private final String username;

    private final String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
