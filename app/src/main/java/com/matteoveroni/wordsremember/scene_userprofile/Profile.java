package com.matteoveroni.wordsremember.scene_userprofile;

import com.matteoveroni.wordsremember.users.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@EqualsAndHashCode
public class Profile {

    @Getter
    private final Long id;
    @Getter
    private final String name;
    @Getter
    @Setter
    private User user;

    public Profile(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
