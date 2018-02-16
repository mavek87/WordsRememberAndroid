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
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
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
    private GoogleSignInRequest googleSignInRequest;

    public LoginPresenter(Settings settings, DBManager dbManager) {
        this.settings = settings;
        this.dbManager = dbManager;
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        setupGoogleSignInComponents();
    }

    public void doAutoLogin() {
        try {
            doOfflineLoginIfUserAlreadyRegistered();
        } catch (Settings.NoRegisteredUserException e) {
            generateAndSendGoogleSignInRequest();
        }
    }

    public void onSignInActionFromView() {
        generateAndSendGoogleSignInRequest();
    }

    public void handleGoogleSignInRequestResult(GoogleSignInRequestResult signInRequestResult) {
        switch (signInRequestResult.getRequestCode()) {

            case GOOGLE_SIGN_IN_REQUEST_CODE:
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(signInRequestResult.getSignInResultIntent());

                if (signInResult == null || !signInResult.isSuccess()) {
                    view.showSignInErrorPopup("Sign in failed");
                    break;
                }

                int signInStatusCode = signInResult.getStatus().getStatusCode();
                String signInStatusName = GoogleSignInStatusCodes.getStatusCodeString(signInStatusCode);

                try {
                    User user = getLoginUser(signInResult);
                    saveLoginUser(user);
                    FormattedString message = buildSuccessfulLoginMessage(user);
                    loginAndShowMessage(message);
                } catch (Exception ex) {
                    view.showSignInErrorPopup(signInStatusName);
                    Log.e(TAG, ex.getMessage());
                }
                break;

            default:
                throw new RuntimeException("Unknown google sign in request code!");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showSignInErrorPopup(connectionResult.getErrorMessage());
    }

    private void setupGoogleSignInComponents() {
        if (googleSignInOptionsComponent == null) {
            googleSignInOptionsComponent = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
        }

        if (googleApiClientComponent == null) {
            FragmentActivity activityView = (FragmentActivity) this.view;
            googleApiClientComponent = new GoogleApiClient.Builder(activityView)
                    .enableAutoManage(activityView, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptionsComponent)
                    .build();
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

        loginAndShowMessage(message);
    }

    private void loginAndShowMessage(FormattedString message) {
        view.showSuccessfulSignInPopup(message);
        if (settings.isAppStartedForTheFirstTime()) {
            view.switchToView(View.Name.USER_PROFILE_FIRST_CREATION);
        } else {
            view.switchToView(View.Name.USER_PROFILES_MANAGEMENT);
        }
        view.destroy();
    }

    private void generateAndSendGoogleSignInRequest() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClientComponent);
        googleSignInRequest = new GoogleSignInRequest(GOOGLE_SIGN_IN_REQUEST_CODE, signInIntent);
        view.sendGoogleSignInRequest(googleSignInRequest);
    }

    private User getLoginUser(GoogleSignInResult signInResult) {
        GoogleSignInAccount google_account = signInResult.getSignInAccount();
        String google_username = google_account.getDisplayName();
        String google_email = google_account.getEmail();
        return new User(google_username, google_email);
    }

    private void saveLoginUser(User user) {
        settings.saveUser(user);
//        dbManager.loadUserDBHelper(user);
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
