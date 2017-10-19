package com.matteoveroni.wordsremember.login;

import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface LoginView extends View {

    void doGoogleSignIn();

    void showSuccessfulSignInPopup(String message);

    void showSignInErrorPopup(String errorMessage);

    void destroy();
}
