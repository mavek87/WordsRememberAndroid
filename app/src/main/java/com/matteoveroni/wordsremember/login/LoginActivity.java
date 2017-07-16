package com.matteoveroni.wordsremember.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;

/**
 * Login Activity
 *
 * @author Matteo Veroni
 *
 * @links https://developers.google.com/identity/sign-in/android/sign-in
 */

public class LoginActivity extends BaseActivityPresentedView implements LoginView {

    public static final String TAG = TagGenerator.tag(LoginActivity.class);

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 5555;

    private LoginPresenter presenter;
    private GoogleApiClient googleApiClient;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new LoginPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (LoginPresenter) presenter;

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this.presenter)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton btn_signIn = (SignInButton) findViewById(R.id.login_btn_signin);
        btn_signIn.setSize(SignInButton.SIZE_STANDARD);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleApiClient != null) {
                    Intent intent_signInAttempt = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent_signInAttempt, GOOGLE_SIGN_IN_REQUEST_CODE);
                }
            }
        });

        setupAndShowToolbar(getString(R.string.app_name) + " - Login");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.onSignInAttempt(signInResult);
        }
    }

    @Override
    public void doLogin() {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
    }

    @Override
    public void showSuccessfulMessage(String message) {
        showMessage(getString(R.string.msg_wrong_status) + "\n\n" + message);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        showMessage(getString(R.string.msg_wrong_status) + "\n\n" + errorMessage);
    }

}

