package com.matteoveroni.wordsremember.dictionary.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.view.ViewPojoUser;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment designed to manage CRUD operations on single vocables in restoreViewData dictionary
 *
 * @author Matteo Veroni
 */

public class DictionaryManipulationFragment extends Fragment implements ViewPojoUser<Word> {

    public static final String TAG = TagGenerator.tag(DictionaryManipulationFragment.class);

    private final static String VOCABLE_CONTENT_KEY = "VOCABLE_CONTENT_KEY";
    private final static String VIEW_TITLE_CONTENT_KEY = "VIEW_TITLE_CONTENT_KEY";
    private final static String VIEW_VOCABLE_NAME_CONTENT_KEY = "VIEW_VOCABLE_NAME_CONTENT_KEY";
    private Unbinder viewInjector;

    private Word vocableInView;

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
    public Word getPojoUsedByView() {
        Word currentVocableInView = new Word(txt_vocableName.getText().toString());
        currentVocableInView.setId(vocableInView.getId());
        return currentVocableInView;
    }

    @Override
    public void setPojoUsedInView(Word vocableToShow) {
        if (vocableToShow == null) {
            throw new IllegalArgumentException("Error setting pojo for DictionaryManipulationFragment. Vocable passed cannot be null!");
        }
        if (vocableToShow.getId() <= 0) {
            lbl_title.setText("Create vocable");
            txt_vocableName.setText("");
        } else {
            lbl_title.setText("Edit vocable");
            txt_vocableName.setText(vocableToShow.getName());
        }
        vocableInView = vocableToShow;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveViewData(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        restoreViewData(savedInstanceState);
    }

    private void saveViewData(Bundle instanceState) {
        saveViewTitle(instanceState);
        saveVocableUsedByView(instanceState);
        saveVocableNameTextView(instanceState);
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState != null) {
            restoreViewTitle(instanceState);
            restoreVocableUsedByView(instanceState);
            restoreVocableNameTextView(instanceState);
        }
    }

    private void saveViewTitle(Bundle instanceState) {
        instanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
    }

    private void restoreViewTitle(Bundle instanceState) {
        if (instanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
            lbl_title.setText(instanceState.getString(VIEW_TITLE_CONTENT_KEY));
        }
    }

    private void saveVocableUsedByView(Bundle instanceState) {
        instanceState.putString(VOCABLE_CONTENT_KEY, vocableInView.toJson());
    }

    private void restoreVocableUsedByView(Bundle instanceState) {
        if (instanceState.containsKey(VOCABLE_CONTENT_KEY)) {
            vocableInView = Word.fromJson(instanceState.getString(VOCABLE_CONTENT_KEY));
        }
    }

    private void saveVocableNameTextView(Bundle instanceState) {
        instanceState.putString(VIEW_VOCABLE_NAME_CONTENT_KEY, txt_vocableName.getText().toString());
    }

    private void restoreVocableNameTextView(Bundle instanceState) {
        if (instanceState.containsKey(VIEW_VOCABLE_NAME_CONTENT_KEY)) {
            txt_vocableName.setText(instanceState.getString(VIEW_VOCABLE_NAME_CONTENT_KEY));
        }
    }
}
