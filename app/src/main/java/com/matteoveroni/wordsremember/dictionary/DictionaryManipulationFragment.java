package com.matteoveroni.wordsremember.dictionary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.pojo.Word;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment designed to manage CRUD operations on single vocables in a dictionary
 *
 * @author Matteo Veroni
 */
public class DictionaryManipulationFragment extends Fragment {

    public static final String TAG = "F_DICTIONARY_MANIPULATION";

    private final static String TITLE_CONTENT_KEY = "TITLE_KEY";
    private final static String VOCABLE_NAME_CONTENT_KEY = "VOCABLE_NAME_KEY";

    private DictionaryManipulationMode fragmentMode;
    private Unbinder viewInjector;

    private enum DictionaryManipulationMode {
        EDIT, CREATE;
    }

    @BindView(R.id.fragment_dictionary_manipulation_title)
    TextView lbl_title;

    @BindView(R.id.fragment_dictionary_manipulation_txt_vocable_name)
    EditText txt_vocableName;

    public DictionaryManipulationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_manipulation, container, false);
        viewInjector = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        viewInjector.unbind();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_CONTENT_KEY, lbl_title.getText().toString());
        savedInstanceState.putString(VOCABLE_NAME_CONTENT_KEY, txt_vocableName.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VOCABLE_NAME_CONTENT_KEY)) {
                txt_vocableName.setText(savedInstanceState.getString(VOCABLE_NAME_CONTENT_KEY));
            }
        }
    }

    public void populateViewForVocable(Word vocable) {
        if (isFragmentCreated()) {
            if (vocable == null || vocable.getName() == null) {
                lbl_title.setText("Create vocable");
                txt_vocableName.setText("");
                fragmentMode = DictionaryManipulationMode.CREATE;
            } else {
                lbl_title.setText("View vocable");
                txt_vocableName.setText(vocable.getName());
                fragmentMode = DictionaryManipulationMode.EDIT;
            }
        }
    }

    private boolean isFragmentCreated() {
        return getView() != null && lbl_title != null && txt_vocableName != null;
    }
}
