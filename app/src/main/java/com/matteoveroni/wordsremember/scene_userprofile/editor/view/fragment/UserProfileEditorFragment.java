package com.matteoveroni.wordsremember.scene_userprofile.editor.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorFragment extends Fragment implements PojoManipulable<UserProfile>, AdapterView.OnItemSelectedListener {
    private static final String TAG = TagGenerator.tag(UserProfileEditorFragment.class);

    private static final String VIEW_TITLE_CONTENT_KEY = "view_title_content_key";
    private static final String VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY = "view_user_profile_name_textview_content_key";

    private Unbinder butterknifeBinder;
    private long id_userProfileToEdit;

    @BindView(R.id.fragment_user_profile_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_user_profile_editor_spinnerFirstLocale)
    SearchableSpinner spinnerFirstLocale;

    @BindView(R.id.fragment_user_profile_editor_spinnerSecondLocale)
    SearchableSpinner spinnerSecondLocale;

    @BindView(R.id.fragment_user_profile_editor_txt_dictionary_name)
    EditText txt_dictionaryName;

    private Map<String, Locale> availableLocalesStringified;

    private int firstSpinnerID;
    private int secondSpinnerID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_editor, container, false);
        butterknifeBinder = ButterKnife.bind(this, view);

        firstSpinnerID = spinnerFirstLocale.getId();
        secondSpinnerID = spinnerSecondLocale.getId();

        availableLocalesStringified = LocaleTranslator.getAvailableLocalesStringified();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, availableLocalesStringified.keySet().toArray());
        spinnerFirstLocale.setAdapter(adapter);
        spinnerFirstLocale.setOnItemSelectedListener(this);
        spinnerFirstLocale.setTitle("Select language");

        spinnerSecondLocale.setAdapter(adapter);
        spinnerSecondLocale.setOnItemSelectedListener(this);
        spinnerSecondLocale.setTitle("Select language");

        final String str_EnglishLocale = LocaleTranslator.stringifyLocale(Locale.ENGLISH);
        int englishPositionInAdapter = adapter.getPosition(str_EnglishLocale);
        spinnerFirstLocale.setSelection(englishPositionInAdapter);

        final String str_locale = LocaleTranslator.stringifyLocale(LocaleTranslator.getLocale(getContext()));
        if (str_locale != null && !str_locale.trim().isEmpty()) {
            int localePositionInAdapter = adapter.getPosition(str_locale);
            spinnerSecondLocale.setSelection(localePositionInAdapter);
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final int parentID = parent.getId();
        String text = txt_dictionaryName.getText().toString();
        String[] textDivided = text.split(" - ");

        if (parentID == firstSpinnerID) {
            Locale firstSpinnerLocale = availableLocalesStringified.get(spinnerFirstLocale.getSelectedItem());
            String languageToDisplay = firstSpinnerLocale.getDisplayLanguage(firstSpinnerLocale);

            if (textDivided.length > 2) {
                String remainingTextToDisplay = "";
                for (int i = 1; i < textDivided.length; i++) {
                    remainingTextToDisplay += textDivided[i];
                    if ((i + 1) < textDivided.length) {
                        remainingTextToDisplay += " - ";
                    }
                }
                txt_dictionaryName.setText(languageToDisplay + " - " + remainingTextToDisplay);
            } else if (textDivided.length == 2) {
                txt_dictionaryName.setText(languageToDisplay + " - " + textDivided[1]);
            } else if (textDivided.length <= 1) {
                if (text.trim().isEmpty()) {
                    txt_dictionaryName.setText(languageToDisplay);
                } else {
                    txt_dictionaryName.setText(languageToDisplay + " - " + text);
                }
            }
        } else if (parentID == secondSpinnerID) {
            Locale secondSpinnerLocale = availableLocalesStringified.get(spinnerSecondLocale.getSelectedItem());
            String languageToDisplay = secondSpinnerLocale.getDisplayLanguage(secondSpinnerLocale);

            if (textDivided.length >= 2) {
                txt_dictionaryName.setText(textDivided[0] + " - " + languageToDisplay);
            } else if (textDivided.length <= 1) {
                if (text.trim().isEmpty()) {
                    txt_dictionaryName.setText(languageToDisplay);
                } else {
                    txt_dictionaryName.setText(text + " - " + languageToDisplay);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDestroyView() {
        butterknifeBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public UserProfile getPojoUsed() {
        return getUserProfileFromView();
    }

    private UserProfile getUserProfileFromView() {
        return new UserProfile(id_userProfileToEdit, txt_dictionaryName.getText().toString());
    }

    @Override
    public void setPojoUsed(UserProfile userProfile) {
        id_userProfileToEdit = userProfile.getId();
        if (id_userProfileToEdit <= 0) {
            lbl_title.setText(R.string.create_user_profile);
        } else {
            lbl_title.setText(R.string.edit_user_profile);
        }
        txt_dictionaryName.setText(userProfile.getName());
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
        instanceState.putString(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY, txt_dictionaryName.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(VIEW_TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY)) {
                txt_dictionaryName.setText(savedInstanceState.getString(VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY));
            }
        }
    }
}
