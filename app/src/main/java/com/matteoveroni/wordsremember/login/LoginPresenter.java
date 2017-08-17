package com.matteoveroni.wordsremember.login;

import android.support.annotation.NonNull;
import android.util.Log;

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
    public static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private LoginView view;

    private final Settings settings;

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

    public void onSignInRequest() {
        view.doSignIn();
    }

    public void handleSignInResult(GoogleSignInResult signInResult) {

        int statusCode = signInResult.getStatus().getStatusCode();
        String statusName = GoogleSignInStatusCodes.getStatusCodeString(statusCode);

        if (signInResult.isSuccess()) {

            GoogleSignInAccount google_account = signInResult.getSignInAccount();
            String google_username = google_account.getDisplayName();
            String google_email = google_account.getEmail();
//            String img_url = account.getPhotoUrl().toString();

            settings.saveUser(new User(google_username, google_email));

            final String status = "Sign in after registering new google user\n\n";
            view.showSuccessfulMessage(status + statusName + "\nName: " + google_username + "\nEmail: " + google_email);
            view.switchTo(View.Name.MAIN_MENU);

        } else {
            view.showErrorMessage(statusName);
        }
    }

    public void tryToSignInUsingRegisteredUser() {
        try {
            User prefs_user = settings.getUser();

            final String status = "Sign in using registerd user\n\n";
            view.showSuccessfulMessage(status + "\nName: " + prefs_user.getUsername() + "\nEmail: " + prefs_user.getEmail());
            view.switchTo(View.Name.MAIN_MENU);
        } catch (Settings.NoRegisteredUserException e) {
            Log.d(TAG, "No registered user");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showErrorMessage(connectionResult.getErrorMessage());
    }
}
