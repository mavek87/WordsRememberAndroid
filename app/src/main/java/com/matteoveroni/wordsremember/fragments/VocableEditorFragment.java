package com.matteoveroni.wordsremember.fragments;

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
import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment designed to manage CRUD operations on single vocables in restoreViewData dictionary
 *
 * @author Matteo Veroni
 */

public class VocableEditorFragment extends Fragment implements PojoManipulable<Word> {

    public static final String TAG = TagGenerator.tag(VocableEditorFragment.class);

    private final static String VOCABLE_CONTENT_KEY = "VOCABLE_CONTENT_KEY";
    private final static String VIEW_TITLE_CONTENT_KEY = "VIEW_TITLE_CONTENT_KEY";
    private final static String VIEW_VOCABLE_NAME_CONTENT_KEY = "VIEW_VOCABLE_NAME_CONTENT_KEY";

    private Unbinder viewInjector;
    private Word vocableInView;

    @BindView(R.id.fragment_vocable_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_vocable_editor_txt_vocable_name)
    EditText txt_vocableName;

    public VocableEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocable_editor, container, false);
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
        return new Word(vocableInView.getId(), txt_vocableName.getText().toString());
    }

    @Override
    public void setPojoUsedInView(Word vocableToShow) {
        if (vocableToShow.getId() <= 0) {
            lbl_title.setText("Create vocable");
        } else {
            lbl_title.setText("Edit vocable");
        }
        txt_vocableName.setText(vocableToShow.getName());
        vocableInView = vocableToShow;
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        saveViewData(instanceState);
    }

    private void saveViewData(Bundle instanceState) {
        instanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
        instanceState.putString(VOCABLE_CONTENT_KEY, vocableInView.toJson());
        instanceState.putString(VIEW_VOCABLE_NAME_CONTENT_KEY, txt_vocableName.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            restoreViewData(savedInstanceState);
        }
    }

    private void restoreViewData(Bundle instanceState) {
        if (instanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
            lbl_title.setText(instanceState.getString(VIEW_TITLE_CONTENT_KEY));
        }
        if (instanceState.containsKey(VOCABLE_CONTENT_KEY)) {
            vocableInView = Word.fromJson(instanceState.getString(VOCABLE_CONTENT_KEY));
        }
        if (instanceState.containsKey(VIEW_VOCABLE_NAME_CONTENT_KEY)) {
            txt_vocableName.setText(instanceState.getString(VIEW_VOCABLE_NAME_CONTENT_KEY));
        }
    }
}
