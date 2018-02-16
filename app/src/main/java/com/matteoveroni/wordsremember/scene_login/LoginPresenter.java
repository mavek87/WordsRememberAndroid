package com.matteoveroni.wordsremember.scene_login;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

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
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter extends BasePresenter<LoginView> implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;
    private final Settings settings;
    private GoogleApiClient googleApiClientComponent;
    private GoogleSignInOptions googleSignInOptionsComponent;
    private GoogleSignInRequest googleSignInRequest;

    public LoginPresenter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        initGoogleSignInComponents();
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

    public void handleGoogleSignInRequestResult() {
        if (googleSignInRequest != null) {

            switch (googleSignInRequest.getRequestCode()) {

                case GOOGLE_SIGN_IN_REQUEST_CODE:
                    GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(googleSignInRequest.getSignInIntent());

                    int signInStatusCode = signInResult.getStatus().getStatusCode();
                    String signInStatusName = GoogleSignInStatusCodes.getStatusCodeString(signInStatusCode);

                    if (signInResult.isSuccess()) {
                        GoogleSignInAccount google_account = signInResult.getSignInAccount();
                        String google_username = google_account.getDisplayName();
                        String google_email = google_account.getEmail();
                        // String img_url = account.getPhotoUrl().toString();

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
                        view.showSignInErrorPopup(signInStatusName);
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown google sign in request code!");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showSignInErrorPopup(connectionResult.getErrorMessage());
    }

    private void initGoogleSignInComponents() {
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

        doLoginAndShowMessage(message);
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

    private void generateAndSendGoogleSignInRequest() {
        googleSignInRequest = new GoogleSignInRequest(GOOGLE_SIGN_IN_REQUEST_CODE, Auth.GoogleSignInApi.getSignInIntent(googleApiClientComponent));
        view.sendGoogleSignInRequest(googleSignInRequest);
    }
}
