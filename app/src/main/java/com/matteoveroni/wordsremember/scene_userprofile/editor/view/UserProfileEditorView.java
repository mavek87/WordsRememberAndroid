package com.matteoveroni.wordsremember.scene_userprofile.editor.view;

import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public interface UserProfileEditorView extends View, PojoManipulable<Profile> {
    void saveProfileAction();

    void returnToPreviousView();

    void onBackPressed();

    void finish();
}
