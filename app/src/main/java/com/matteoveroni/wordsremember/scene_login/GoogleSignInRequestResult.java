package com.matteoveroni.wordsremember.scene_login;

import android.content.Intent;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class GoogleSignInRequestResult {

    @Getter
    private final int requestCode;
    @Getter
    private final Intent signInResultIntent;
}
