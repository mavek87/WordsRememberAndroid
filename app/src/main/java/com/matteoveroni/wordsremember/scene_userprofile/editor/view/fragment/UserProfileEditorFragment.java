package com.matteoveroni.wordsremember.scene_userprofile.editor.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorFragment extends Fragment implements PojoManipulable<UserProfile> {

    public static final String TAG = TagGenerator.tag(UserProfileEditorFragment.class);

    private static final String VIEW_TITLE_CONTENT_KEY = "view_title_content_key";
    //    private static final String PERSISTING_USER_PROFILE_CONTENT_KEY = "persisting_user_profile_content_key";
    private static final String VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY = "view_user_profile_name_textview_content_key";

    private Unbinder butterknifeBinder;
    private UserProfile persistingUserProfile;

    @BindView(R.id.fragment_user_profile_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_user_profile_editor_txt_profile_name)
    EditText txt_userProfileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_editor, container, false);
        butterknifeBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        butterknifeBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public UserProfile getPojoUsed() {
        persistingUserProfile = getUserProfileInView();
        return persistingUserProfile;
    }

    private UserProfile getUserProfileInView() {
        return new UserProfile(persistingUserProfile.getId(), txt_userProfileName.getText().toString());
    }

    @Override
    public void setPojoUsed(UserProfile userProfile) {
        if (userProfile.hasNullOrEmptyName()) {
            lbl_title.setText(R.string.create_user_profile);
        } else {
            lbl_title.setText(R.string.edit_user_profile);
        }
        txt_userProfileName.setText(userProfile.getProfileName());
        this.persistingUserProfile = userProfile;
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
        instanceState.putString(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY, txt_userProfileName.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(VIEW_TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY)) {
                txt_userProfileName.setText(savedInstanceState.getString(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY));
            }
        }
    }
}
