package com.matteoveroni.wordsremember.scene_login;

import android.content.Intent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class GoogleSignInRequest {

    @Getter
    private final int requestCode;
    @Getter
    private final Intent signInIntent;
}