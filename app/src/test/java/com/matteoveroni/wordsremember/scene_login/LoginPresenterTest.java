package com.matteoveroni.wordsremember.scene_login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.scene_settings.exceptions.NoRegisteredUserException;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.users.NotPersistedUser;
import com.matteoveroni.wordsremember.users.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginPresenterTest {

    private LoginPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LoginView view;
    @Mock
    private DBManager dbManager;
    @Mock
    private Settings settings;
    @Mock
    private GoogleSignInAccount googleSignInAccount;
    @Mock
    private GoogleSignInResult googleSignInResult;

    private final User USER = new User(1L, "TestUser", "fakemail@gmail.com");
    private final User GOOGLE_USER = new NotPersistedUser("TestUser", "fakemail@gmail.com");

    private final int VALID_REQUEST_CODE = LoginPresenter.GOOGLE_SIGN_IN_REQUEST_CODE;
    private final int INVALID_REQUEST_CODE = LoginPresenter.GOOGLE_SIGN_IN_REQUEST_CODE + 1;
    private final Status SUCCESSFUL_STATUS = new Status(CommonStatusCodes.SUCCESS);
    private final Status FAILED_STATUS = new Status(CommonStatusCodes.NETWORK_ERROR);

    @Before
    public void setUp() {
        presenter = new LoginPresenter(settings, dbManager);
        presenter.attachView(view);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void onSignInActionIfUserAlreadyRegisteredInSettingsDoOfflineLogin() throws Exception {
        // Setup user in settings mock
        when(settings.getNumberOfRegisteredUsers()).thenReturn(1);
        when(settings.getRegisteredUser()).thenReturn(USER);

        presenter.onSignInFromView();

        verify(view).showSuccessfulSignInPopup(any(FormattedString.class));
        verify(view, never()).showSignInErrorPopup(any(String.class));
        verify(view, never()).sendGoogleSignInRequest();
        verify(dbManager).loadUserDBHelper();
    }

    @Test
    public void onSignInActionIfUserNotRegisteredInSettingsTryToDoOnlineLogin() throws Exception {
        // Setup settings mock
        when(settings.getNumberOfRegisteredUsers()).thenReturn(0);

        presenter.onSignInFromView();

        verify(view).sendGoogleSignInRequest();
    }

    // TODO: fix this test
    @Test
    public void onGoogleSignInRequestSuccessfulSaveUserAndDoLogin() throws Exception {
        // Setup googleSignInAccount mock
        when(googleSignInAccount.getDisplayName()).thenReturn(GOOGLE_USER.getUsername());
        when(googleSignInAccount.getEmail()).thenReturn(GOOGLE_USER.getEmail());
        // Setup googleSignInResult mock
        when(googleSignInResult.getSignInAccount()).thenReturn(googleSignInAccount);
        when(googleSignInResult.isSuccess()).thenReturn(true);
        when(googleSignInResult.getStatus()).thenReturn(SUCCESSFUL_STATUS);
        // Setup request result
        GoogleSignInRequestResult validRequestResult = new GoogleSignInRequestResult(VALID_REQUEST_CODE, googleSignInResult);

        presenter.onGoogleSignInRequestResult(validRequestResult);
//
//        final ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
//        verify(settings).registerUser(savedUser.capture());
//        assertEquals(GOOGLE_USER.getUsername(), savedUser.getValue().getUsername());
//        assertEquals(GOOGLE_USER.getEmail(), savedUser.getValue().getEmail());

        verify(dbManager).loadUserDBHelper();

        verify(view).showSuccessfulSignInPopup(any(FormattedString.class));
    }

    @Test
    public void onGoogleSignInRequestWithInvalidRequestCodeViewShowError() {
        // Setup google sign in account
        when(googleSignInAccount.getDisplayName()).thenReturn(GOOGLE_USER.getUsername());
        when(googleSignInAccount.getEmail()).thenReturn(GOOGLE_USER.getEmail());
        // Setup google sign in result
        when(googleSignInResult.getSignInAccount()).thenReturn(googleSignInAccount);
        when(googleSignInResult.isSuccess()).thenReturn(true);
        when(googleSignInResult.getStatus()).thenReturn(SUCCESSFUL_STATUS);
        // Setup request result
        GoogleSignInRequestResult invalidRequestResult = new GoogleSignInRequestResult(INVALID_REQUEST_CODE, googleSignInResult);

        presenter.onGoogleSignInRequestResult(invalidRequestResult);

        verify(view).showSignInErrorPopup(any(String.class));
    }

    @Test
    public void onGoogleSignInRequestWithFailedStatusViewShowError() {
        // Setup google sign in account
        when(googleSignInAccount.getDisplayName()).thenReturn(GOOGLE_USER.getUsername());
        when(googleSignInAccount.getEmail()).thenReturn(GOOGLE_USER.getEmail());
        // Setup google sign in result
        when(googleSignInResult.getSignInAccount()).thenReturn(googleSignInAccount);
        when(googleSignInResult.isSuccess()).thenReturn(false);
        when(googleSignInResult.getStatus()).thenReturn(FAILED_STATUS);
        // Setup request result
        GoogleSignInRequestResult invalidRequestResult = new GoogleSignInRequestResult(VALID_REQUEST_CODE, googleSignInResult);

        presenter.onGoogleSignInRequestResult(invalidRequestResult);

        verify(view).showSignInErrorPopup(any(String.class));
    }

    @Test
    public void onConnectionFailedDuringSignInViewShowError() {
        final ConnectionResult failedConnectionResult = new ConnectionResult(ConnectionResult.NETWORK_ERROR);

        presenter.onConnectionFailed(failedConnectionResult);

        verify(view).showSignInErrorPopup(any(String.class));
    }

    @Test
    public void afterLoginIfThisUserHasGotSomeProfilesGoToManagementView() throws Exception {
        // Setup a user and his profile in settings
        when(settings.getNumberOfRegisteredUsers()).thenReturn(1);
        when(settings.getRegisteredUser()).thenReturn(GOOGLE_USER);
        when(settings.containsUserProfile()).thenReturn(true);

        presenter.onSignInFromView();

        verify(view).switchToView(View.Name.USER_PROFILES_MANAGEMENT);
    }

    @Test
    public void afterLoginIfThisUserHasntGotAnyProfileGoToUserProfileFirstCreationView() throws Exception {
        // Setup a user and his profile in settings
        when(settings.getNumberOfRegisteredUsers()).thenReturn(1);
        when(settings.getRegisteredUser()).thenReturn(GOOGLE_USER);
        when(settings.containsUserProfile()).thenReturn(false);

        presenter.onSignInFromView();

        verify(view).switchToView(View.Name.USER_PROFILE_FIRST_CREATION);
    }
}

