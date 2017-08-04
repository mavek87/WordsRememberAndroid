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
import com.matteoveroni.wordsremember.persistency.dao.UsersDAO;

/**
 * @author Matteo Veroni
 */

public class LoginPresenter implements Presenter, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = TagGenerator.tag(LoginPresenter.class);

    private LoginView view;

    public static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

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

            GoogleSignInAccount account = signInResult.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
//            String img_url = account.getPhotoUrl().toString();

            // TODO: save this data in the preferences file
            saveData(name, email);

            view.showSuccessfulMessage(statusName + "\nName: " + name + "\nEmail: " + email);
            view.switchTo(View.Name.MAIN_MENU);

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
