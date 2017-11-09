package com.matteoveroni.wordsremember.scene_userprofile.editor.view.activity;

import android.os.Bundle;
import android.widget.Toast;

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
 * User Profile Management Activity
 *
 * @author Matteo Veroni
 */

public class UserProfileEditorActivity extends BaseActivityPresentedView implements UserProfileEditorView {

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
        setupAndShowToolbar(getString(R.string.user_profile));
    }



    @Override
    public void saveProfileAction() {
        presenter.onSaveProfileAction(getPojoUsed());
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

