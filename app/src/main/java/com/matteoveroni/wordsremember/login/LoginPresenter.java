package com.matteoveroni.wordsremember.login;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter implements Presenter, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);

    static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private final Settings settings;
    private LoginView view;

    public LoginPresenter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void attachView(Object view) {
        this.view = (LoginView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onGoogleSignInRequest() {
        view.doGoogleSignIn();
    }

    public void handleGoogleSignInResult(GoogleSignInResult signInResult) {
        int statusCode = signInResult.getStatus().getStatusCode();
        String statusName = GoogleSignInStatusCodes.getStatusCodeString(statusCode);

        if (signInResult.isSuccess()) {

            GoogleSignInAccount google_account = signInResult.getSignInAccount();
            String google_username = google_account.getDisplayName();
            String google_email = google_account.getEmail();
//            String img_url = account.getPhotoUrl().toString();

            settings.saveUser(new User(google_username, google_email));

            // TODO: localize this text
            final String message = "Google Sign in successful. New user created.\n\n"
                    + statusName
                    + "\nName: " + google_username + "\nEmail: " + google_email;

            doLoginAndShowMessage(message);

        } else {
            view.showSignInErrorPopup(statusName);
        }
    }

    public void doAutoLogin() {
        try {
            doOfflineLoginIfUserAlreadyRegistered();
        } catch (Settings.NoRegisteredUserException e) {
            view.doGoogleSignIn();
        }
    }

    private void doOfflineLoginIfUserAlreadyRegistered() throws Settings.NoRegisteredUserException {
        User prefs_user = settings.getUser();

        // TODO: localize this text
        final String message = "Sign in successful\n\n"
                + "\nName: " + prefs_user.getUsername() + "\nEmail: " + prefs_user.getEmail();

        doLoginAndShowMessage(message);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showSignInErrorPopup(connectionResult.getErrorMessage());
    }

    private void doLoginAndShowMessage(String message) {
        view.showSuccessfulSignInPopup(message);
        view.switchToView(View.Name.MAIN_MENU);
        view.destroy();
    }
}
