package com.matteoveroni.wordsremember.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class User {

    @Getter
    private final long id;
    @Getter
    private final String username;
    @Getter
    private final String email;
}
