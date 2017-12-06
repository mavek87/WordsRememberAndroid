package com.matteoveroni.wordsremember.scene_userprofile.creation.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.editor.presenter.UserProfileEditorPresenter;
import com.matteoveroni.wordsremember.scene_userprofile.editor.presenter.UserProfileEditorPresenterFactory;
import com.matteoveroni.wordsremember.scene_userprofile.editor.view.UserProfileEditorView;
import com.matteoveroni.wordsremember.scene_userprofile.editor.view.fragment.UserProfileEditorFragment;

import butterknife.ButterKnife;

/**
 * User Profile Editor Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileFirstCreation extends BaseActivityPresentedView implements UserProfileEditorView {

    private UserProfileEditorPresenter presenter;
    private UserProfileEditorFragment userProfileEditorFragment;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new UserProfileEditorPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (UserProfileEditorPresenter) presenter;
    }

    @Override
    public UserProfile getPojoUsed() {
        return userProfileEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(UserProfile pojo) {
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
        // TODO: everyone uses R.menu.menu_dictionary_top_bar. rename the top bar and check his file for refactoring
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
    public void saveProfileAction() {
        presenter.onSaveProfileAction();
    }

    @Override
    public void returnToPreviousView() {
        setResult(RESULT_OK);
        finish();
    }
}
