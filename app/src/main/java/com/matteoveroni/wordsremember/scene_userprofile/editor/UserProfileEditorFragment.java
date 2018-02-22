package com.matteoveroni.wordsremember.scene_userprofile.editor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.scene_userprofile.editor.interfaces.UserProfileEditorView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class UserProfileEditorFragment extends Fragment implements PojoManipulable<Profile>, AdapterView.OnItemSelectedListener, UserProfileEditorView {
    private static final String VIEW_TITLE_CONTENT_KEY = "view_title_content_key";
    private static final String VIEW_USER_PROFILE_NAME_TEXTVIEW_CONTENT_KEY = "view_user_profile_name_textview_content_key";

    @BindView(R.id.fragment_user_profile_editor_title)
    TextView lbl_title;
    @BindView(R.id.fragment_user_profile_editor_spinnerFirstLocale)
    SearchableSpinner spinnerFirstLocale;
    @BindView(R.id.fragment_user_profile_editor_spinnerSecondLocale)
    SearchableSpinner spinnerSecondLocale;
    @BindView(R.id.fragment_user_profile_editor_txt_dictionary_name)
    EditText txt_dictionaryName;

    private Map<String, Locale> availableLocalesStringified;
    private Unbinder butterknifeBinder;

    private int firstSpinnerID;
    private int secondSpinnerID;

    @Override
    public void setHeader(String headerText) {
        lbl_title.setText(headerText);
    }

    @Override
    public void setProfileName(String profileName) {
        txt_dictionaryName.setText(profileName);
    }

    @Override
    public String getProfileName() {
        return txt_dictionaryName.getText().toString();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_editor, container, false);
        butterknifeBinder = ButterKnife.bind(this, view);

        firstSpinnerID = spinnerFirstLocale.getId();
        secondSpinnerID = spinnerSecondLocale.getId();

        availableLocalesStringified = LocaleTranslator.getAvailableLocalesStringified();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, availableLocalesStringified.keySet().toArray());

        setupSpinner(adapter, spinnerFirstLocale, Locale.ENGLISH);
        setupSpinner(adapter, spinnerSecondLocale, LocaleTranslator.getLocale(getContext()));

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
    public Profile getPojoUsed() {
//        return getUserProfileFromView();
        return null;
    }

    @Override
    public void setPojoUsed(Profile userProfile) {
//        id_userProfileToEdit = userProfile.getId();
//        if (id_userProfileToEdit <= 0) {
//            lbl_title.setText(R.string.create_user_profile);
//        } else {
//            lbl_title.setText(R.string.edit_user_profile);
//        }
//        txt_dictionaryName.setText(userProfile.getName());
    }

    // TODO: maybe is possible to move onSaveInstance part of the logic of onSaveInstanceState and on viewStateRestored in the fragment superclass
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

    private void setupSpinner(ArrayAdapter adapter, SearchableSpinner spinner, Locale locale) {
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final String title = getString(R.string.select_language);
        spinner.setTitle(title);

        final String str_locale = LocaleTranslator.stringifyLocale(locale);
        if (!str_locale.trim().isEmpty()) {
            int localePositionInAdapter = adapter.getPosition(str_locale);
            spinner.setSelection(localePositionInAdapter);
        }
    }
}
