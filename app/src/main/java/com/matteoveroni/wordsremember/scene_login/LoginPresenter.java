package com.matteoveroni.wordsremember.scene_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private final Settings settings;
    private final DBManager dbManager;
    private GoogleApiClient googleApiClientComponent;
    private GoogleSignInOptions googleSignInOptionsComponent;

    public LoginPresenter(Settings settings, DBManager dbManager) {
        this.settings = settings;
        this.dbManager = dbManager;
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        setupGoogleSignInComponents();
        view.showMessage(" isAnyUserAlreadySavedInPrefsFile=" + settings.containsUser());
    }

    public void onPostCreateFromView() {
        viewMustDoOfflineLoginIfUserAlreadyRegistered();
    }

    public void onSignInFromView() {
        if (settings.containsUser()) {
            viewMustDoOfflineLoginIfUserAlreadyRegistered();
        } else {
            viewMustDoGoogleSignInRequestToRegisterTheUser();
        }
    }

    // TODO: localize errors
    public void onGoogleSignInRequestResult(@NonNull GoogleSignInRequestResult signInRequestResult) {
        switch (signInRequestResult.getRequestCode()) {
            case GOOGLE_SIGN_IN_REQUEST_CODE:
                final GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(signInRequestResult.getSignInResultIntent());

                if (googleSignInResult == null || !googleSignInResult.isSuccess()) {
                    view.showSignInErrorPopup("Google sign in result is failed. Check your network connection!");
                    break;
                }

                final int signInStatusCode = googleSignInResult.getStatus().getStatusCode();
                final String signInStatusName = GoogleSignInStatusCodes.getStatusCodeString(signInStatusCode);

                try {
                    final User user = getUserFromGoogleSignInResult(googleSignInResult);
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

    private void viewMustDoGoogleSignInRequestToRegisterTheUser() {
        Intent clientGoogleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClientComponent);
        view.sendGoogleSignInRequest(new GoogleSignInRequest(GOOGLE_SIGN_IN_REQUEST_CODE, clientGoogleSignInIntent));
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
        view.showMessage(" isAnyUserAlreadySavedInPrefsFile=" + settings.containsUser());
        view.destroy();
    }

    private User getUserFromGoogleSignInResult(GoogleSignInResult signInResult) {
        GoogleSignInAccount google_account = signInResult.getSignInAccount();
        String google_username = google_account.getDisplayName();
        String google_email = google_account.getEmail();
        return new User(google_username, google_email);
    }

    private void setupGoogleSignInComponents() {
        buildGoogleSignInOptionsComponent();
        buildGoogleApiClientComponent();
    }

    private void buildGoogleSignInOptionsComponent() {
        if (googleSignInOptionsComponent == null) {
            googleSignInOptionsComponent = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
        }
    }

    private void buildGoogleApiClientComponent() {
        if (googleApiClientComponent == null) {
            FragmentActivity activityView = (FragmentActivity) this.view;
            googleApiClientComponent = new GoogleApiClient.Builder(activityView)
                    .enableAutoManage(activityView, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptionsComponent)
                    .build();
        }
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
