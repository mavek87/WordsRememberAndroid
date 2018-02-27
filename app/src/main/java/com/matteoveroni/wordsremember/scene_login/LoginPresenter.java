package com.matteoveroni.wordsremember.scene_login;

import android.support.annotation.NonNull;
import android.util.Log;

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
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.persistency.commands.CommandStoreAndSetAppUser;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.EmptyProfile;
import com.matteoveroni.wordsremember.scene_userprofile.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter extends BasePresenter<LoginView> implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);
    public static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private final Settings settings;
    private final DBManager dbManager;

    public LoginPresenter(Settings settings, DBManager dbManager) {
        this.settings = settings;
        this.dbManager = dbManager;
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
    }

    public void onPostCreateFromView() {
        viewMustDoOfflineLoginIfUserAlreadyRegistered();
    }

    public void onSignInFromView() {
        if (settings.containsUser()) {
            viewMustDoOfflineLoginIfUserAlreadyRegistered();
        } else {
            view.sendGoogleSignInRequest();
        }
    }

    // TODO: localize errors
    public void onGoogleSignInRequestResult(@NonNull GoogleSignInRequestResult signInRequestResult) {
        switch (signInRequestResult.getRequestCode()) {
            case GOOGLE_SIGN_IN_REQUEST_CODE:
                final GoogleSignInResult googleSignInResult = signInRequestResult.getSignInResult();

                if (googleSignInResult == null || !googleSignInResult.isSuccess()) {
                    view.showSignInErrorPopup("Google sign in result is failed. Check your network connection!");
                    break;
                }

                final int signInStatusCode = googleSignInResult.getStatus().getStatusCode();
                final String signInStatusName = GoogleSignInStatusCodes.getStatusCodeString(signInStatusCode);

                try {
                    final User user = createUserFromGoogleSignInResult(googleSignInResult);
                    final FormattedString loginMessageForUser = buildSuccessfulLoginMessage(user);
                    loginAndShowMessage(user, loginMessageForUser);
                } catch (Exception ex) {
                    final String errorMessage = String.format("%s - %s", signInStatusName, "Error trying to sign in");
                    view.showSignInErrorPopup(errorMessage);
                    Log.e(TAG, errorMessage);
                }
                break;
            default:
                view.showSignInErrorPopup("Unknown google sign in request code!");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showSignInErrorPopup("Connection failed - " + connectionResult.getErrorMessage());
    }

    private void viewMustDoOfflineLoginIfUserAlreadyRegistered() {
        try {
            User user = settings.getUser();
            FormattedString message = new FormattedString(
                    "%s\n\n%s: %s\n%s: %s",
                    AndroidLocaleKey.SIGN_IN_SUCCESSFUL.getKeyName(),
                    AndroidLocaleKey.NAME.getKeyName(),
                    user.getUsername(),
                    AndroidLocaleKey.EMAIL.getKeyName(),
                    user.getEmail());

            loginAndShowMessage(user, message);
        } catch (Settings.NoRegisteredUserException ignored) {
        }
    }

    private void loginAndShowMessage(User user, FormattedString message) {
        new CommandStoreAndSetAppUser(user, settings, dbManager).execute();
        view.showSuccessfulSignInPopup(message);

        if (settings.containsUserProfile()) {
            view.switchToView(View.Name.USER_PROFILES_MANAGEMENT);
        } else {
            EVENT_BUS.postSticky(new EventEditUserProfile(new EmptyProfile()));
            view.switchToView(View.Name.USER_PROFILE_FIRST_CREATION);
        }
        view.destroy();
    }

    private User createUserFromGoogleSignInResult(GoogleSignInResult signInResult) {
        GoogleSignInAccount googleAccount = signInResult.getSignInAccount();
        String google_username = googleAccount.getDisplayName();
        String google_email = googleAccount.getEmail();
        return new User(google_username, google_email);
    }

    private FormattedString buildSuccessfulLoginMessage(User user) {
        return new FormattedString(
                "%s, %s\n\n%s: %s\n%s: %s",
                AndroidLocaleKey.USER_REGISTERED.getKeyName(),
                AndroidLocaleKey.SIGN_IN_SUCCESSFUL.getKeyName(),
                AndroidLocaleKey.NAME.getKeyName(),
                user.getUsername(),
                AndroidLocaleKey.EMAIL.getKeyName(),
                user.getEmail());
    }
}
