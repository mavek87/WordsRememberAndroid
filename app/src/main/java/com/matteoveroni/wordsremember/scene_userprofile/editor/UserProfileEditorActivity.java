package com.matteoveroni.wordsremember.scene_userprofile.editor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.editor.interfaces.UserProfileEditorExtendedView;

import butterknife.ButterKnife;

/**
 * User Profile Editor Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileEditorActivity extends AbstractPresentedActivityView implements UserProfileEditorExtendedView {

    private UserProfileEditorPresenter presenter;
    private UserProfileEditorFragment userProfileEditorFragment;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.USER_PROFILE_EDITOR_PRESENTER);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (UserProfileEditorPresenter) presenter;
    }

    @Override
    public Profile getPojoUsed() {
        return userProfileEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(Profile pojo) {
        userProfileEditorFragment.setPojoUsed(pojo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_editor);
        ButterKnife.bind(this);
        userProfileEditorFragment = (UserProfileEditorFragment) getSupportFragmentManager().findFragmentById(R.id.user_profile_editor_fragment);
        setupAndShowToolbar(getString(R.string.user_profile_editor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_top_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_done:
                saveProfileAction();
                return true;
        }
        return false;
    }

    @Override
    public void setHeader(String headerText) {
        userProfileEditorFragment.setHeader(headerText);
    }

    @Override
    public void setProfileName(String profileName) {
        userProfileEditorFragment.setProfileName(profileName);
    }

    @Override
    public String getProfileName() {
        return userProfileEditorFragment.getProfileName();
    }

    @Override
    public void saveProfileAction() {
        presenter.onSaveProfileAction();
    }

    @Override
    public void returnToPreviousView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}

