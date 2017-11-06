package com.matteoveroni.wordsremember.scene_dictionary.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

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

    private final static String VIEW_TITLE_CONTENT_KEY = "VIEW_TITLE_CONTENT_KEY";
    private final static String VIEW_VOCABLE_TEXTVIEW_CONTENT_KEY = "VIEW_VOCABLE_TEXTVIEW_CONTENT_KEY";
    private final static String VOCABLE_EDITED_CONTENT_KEY = "VOCABLE_EDITED_CONTENT_KEY";

    private Unbinder butterknifeUnbinder;
    private Word vocableToEdit;

    @BindView(R.id.fragment_vocable_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_vocable_editor_txt_vocable_name)
    EditText txt_vocableName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocable_editor, container, false);
        butterknifeUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        butterknifeUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public Word getPojoUsed() {
        return new Word(vocableToEdit.getId(), txt_vocableName.getText().toString());
    }

    @Override
    public void setPojoUsed(Word vocable) {
        if (vocable.getId() <= 0) {
            lbl_title.setText(getString(R.string.create_vocable));
        } else {
            lbl_title.setText(getString(R.string.edit_vocable));
            if (txt_vocableName.getText().toString().trim().isEmpty()) {
                txt_vocableName.setText(vocable.getName());
            }
        }
        vocableToEdit = vocable;
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
        instanceState.putString(VOCABLE_EDITED_CONTENT_KEY, vocableToEdit.toJson());
        instanceState.putString(VIEW_VOCABLE_TEXTVIEW_CONTENT_KEY, txt_vocableName.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(VIEW_TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VOCABLE_EDITED_CONTENT_KEY)) {
                vocableToEdit = Word.fromJson(savedInstanceState.getString(VOCABLE_EDITED_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VIEW_VOCABLE_TEXTVIEW_CONTENT_KEY)) {
                txt_vocableName.setText(savedInstanceState.getString(VIEW_VOCABLE_TEXTVIEW_CONTENT_KEY));
            }
        }
    }
}
