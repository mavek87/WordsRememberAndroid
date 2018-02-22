package com.matteoveroni.wordsremember.scene_userprofile.editor.interfaces;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public interface UserProfileEditorExtendedView extends View, UserProfileEditorView, PojoManipulable<Profile> {

    void saveProfileAction();

    void returnToPreviousView();

    void onBackPressed();

    void finish();
}
