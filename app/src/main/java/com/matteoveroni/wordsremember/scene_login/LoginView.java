package com.matteoveroni.wordsremember.scene_login;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface LoginView extends View {

    void sendGoogleSignInRequest();

    void showSuccessfulSignInPopup(FormattedString message);

    void showSignInErrorPopup(String errorMessage);

    void destroy();
}
