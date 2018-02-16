package com.matteoveroni.wordsremember.scene_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.SignInButton;
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

/**
 * Login Activity
 *
 * @author Matteo Veroni
 * @links https://developers.google.com/identity/sign-in/android/sign-in
 * https://android-developers.googleblog.com/2016/03/registering-oauth-clients-for-google.html
 * https://stackoverflow.com/questions/36361956/how-to-add-multiple-system-sha-keys-in-google-single-sign-on-gson
 */

public class LoginActivity extends AbstractPresentedActivityView implements LoginView {

    public static final String TAG = TagGenerator.tag(LoginActivity.class);

    private LoginPresenter presenter;

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
        ButterKnife.bind(this);
        btn_signIn.setSize(SignInButton.SIZE_STANDARD);
        setupAndShowToolbar(getString(R.string.app_name) + " - Login");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.doAutoLogin();
    }

    @OnClick(R.id.login_btn_signin)
    public void onSignInButtonPressed() {
        presenter.onSignInActionFromView();
    }

    @Override
    public void sendGoogleSignInRequest(GoogleSignInRequest request) {
        startActivityForResult(request.getSignInIntent(), request.getRequestCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent signInRequestResultIntent) {
        super.onActivityResult(requestCode, resultCode, signInRequestResultIntent);
        presenter.handleGoogleSignInRequestResult(new GoogleSignInRequestResult(requestCode, signInRequestResultIntent));
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



