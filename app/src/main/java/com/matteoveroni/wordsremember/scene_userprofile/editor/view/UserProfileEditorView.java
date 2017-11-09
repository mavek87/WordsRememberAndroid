package com.matteoveroni.wordsremember.scene_userprofile.editor.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

/**
 * @author Matteo Veroni
 */

public interface UserProfileEditorView extends View, PojoManipulable<UserProfile> {
    void saveProfileAction();

    void returnToPreviousView();

    void onBackPressed();
}
