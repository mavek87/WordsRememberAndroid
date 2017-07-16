package com.matteoveroni.wordsremember.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter implements Presenter, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);

    private LoginView view;

    @Override
    public void attachView(Object view) {
        this.view = (LoginView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void onSignInAttempt(GoogleSignInResult signInResult) {
        Status signInStatus = signInResult.getStatus();

        if (signInResult.isSuccess()) {
            GoogleSignInAccount account = signInResult.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String img_url = account.getPhotoUrl().toString();

            Log.d(TAG, "name: " + name + "\nemail: " + email);
            view.doLogin();
        } else {
            String errorMessage = signInStatus.getStatusMessage();
            view.showMessage(errorMessage);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showMessage(connectionResult.getErrorMessage());
    }
}
