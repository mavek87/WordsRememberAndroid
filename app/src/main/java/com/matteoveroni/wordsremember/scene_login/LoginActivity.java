package com.matteoveroni.wordsremember.scene_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.matteoveroni.wordsremember.scene_login.LoginPresenter.GOOGLE_SIGN_IN_REQUEST_CODE;

/**
 * Login Activity
 *
 * @author Matteo Veroni
 * @links https://developers.google.com/identity/sign-in/android/sign-in
 * https://android-developers.googleblog.com/2016/03/registering-oauth-clients-for-google.html
 * https://stackoverflow.com/questions/36361956/how-to-add-multiple-system-sha-keys-in-google-single-sign-on-gson
 */

public class LoginActivity extends AbstractPresentedActivityView implements LoginView {

    public static final String TAG = TagGenerator.tag(com.matteoveroni.wordsremember.scene_login.LoginActivity.class);

    private LoginPresenter presenter;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;

    @BindView(R.id.login_btn_signin)
    SignInButton btn_signIn;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.LOGIN_PRESENTER_FACTORY);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (LoginPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupAndShowToolbar(getString(R.string.app_name) + " - Login");
        ButterKnife.bind(this);
        btn_signIn.setSize(SignInButton.SIZE_STANDARD);

        if (googleSignInOptions == null) {
            googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, presenter)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.onPostCreateFromView();
    }

    @OnClick(R.id.login_btn_signin)
    public void onSignInAction() {
        presenter.onSignInFromView();
    }

    @Override
    public void sendGoogleSignInRequest() {
        Intent clientGoogleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(clientGoogleSignInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent signInRequestResultIntent) {
        super.onActivityResult(requestCode, resultCode, signInRequestResultIntent);
        final GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(signInRequestResultIntent);
        presenter.onGoogleSignInRequestResult(new GoogleSignInRequestResult(requestCode, googleSignInResult));
    }

    @Override
    public void showSuccessfulSignInPopup(FormattedString message) {
        showMessage(getString(R.string.msg_complete_status) + "\n\n" + localize(message));
    }

    @Override
    public void showSignInErrorPopup(String errorMessage) {
        showMessage(getString(R.string.msg_wrong_status) + "\n\n" + errorMessage);
    }

    @Override
    public void destroy() {
        finish();
    }
}



