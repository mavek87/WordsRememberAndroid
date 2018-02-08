package com.matteoveroni.wordsremember.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

public class User {
    @Getter
    @Setter
    private long id;
    @Getter
    private final String username;
    @Getter
    private final String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
