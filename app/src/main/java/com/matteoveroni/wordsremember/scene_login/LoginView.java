package com.matteoveroni.wordsremember.scene_login;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface LoginView extends View {

    void sendGoogleSignInRequest(GoogleSignInRequest signInRequest);

    void showSuccessfulSignInPopup(FormattedString message);

    void showSignInErrorPopup(String errorMessage);

    void destroy();
}
