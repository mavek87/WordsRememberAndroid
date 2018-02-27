package com.matteoveroni.wordsremember.scene_login;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

public class LoginPresenterFactory implements PresenterFactory {

    @Inject
    Settings settings;

    @Inject
    DBManager dbManager;

    @Override
    public LoginPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new LoginPresenter(settings, dbManager);
    }
}
