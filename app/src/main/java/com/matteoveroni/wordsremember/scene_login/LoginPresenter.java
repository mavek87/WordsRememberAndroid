package com.matteoveroni.wordsremember.scene_login;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.AndroidLocaleKey;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter extends BasePresenter<LoginView> implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);

    static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private final Settings settings;

    public LoginPresenter(Settings settings) {
        this.settings = settings;
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

            FormattedString message = new FormattedString(
                    "%s, %s\n\n%s: %s\n%s: %s",
                    AndroidLocaleKey.USER_REGISTERED.getKeyName(),
                    AndroidLocaleKey.SIGN_IN_SUCCESSFUL.getKeyName(),
                    AndroidLocaleKey.NAME.getKeyName(),
                    google_username,
                    AndroidLocaleKey.EMAIL.getKeyName(),
                    google_email);

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

        FormattedString message = new FormattedString(
                "%s\n\n%s: %s\n%s: %s",
                AndroidLocaleKey.SIGN_IN_SUCCESSFUL.getKeyName(),
                AndroidLocaleKey.NAME.getKeyName(),
                prefs_user.getUsername(),
                AndroidLocaleKey.EMAIL.getKeyName(),
                prefs_user.getEmail());

        doLoginAndShowMessage(message);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showSignInErrorPopup(connectionResult.getErrorMessage());
    }

    private void doLoginAndShowMessage(FormattedString message) {
        view.showSuccessfulSignInPopup(message);
        if (settings.isAppStartedForTheFirstTime()) {
            view.switchToView(View.Name.USER_PROFILE_FIRST_CREATION);
        } else {
            view.switchToView(View.Name.USER_PROFILES_MANAGEMENT);
        }
        view.destroy();
    }
}
