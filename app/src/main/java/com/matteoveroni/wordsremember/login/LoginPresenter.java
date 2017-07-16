package com.matteoveroni.wordsremember.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
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

    void onSignInAttempt(GoogleSignInResult result) {

        int statusCode = result.getStatus().getStatusCode();
        String statusName = GoogleSignInStatusCodes.getStatusCodeString(statusCode);

        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
//            String img_url = account.getPhotoUrl().toString();

            // TODO: save this data in the preferences file
            saveData(name, email);

            view.showSuccessfulMessage(statusName + "\nName: " + name + "\nEmail: " + email);
            view.doLogin();

        } else {
            view.showErrorMessage(statusName);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        view.showErrorMessage(connectionResult.getErrorMessage());
    }

    private void saveData(String name, String email) {
    }
}
