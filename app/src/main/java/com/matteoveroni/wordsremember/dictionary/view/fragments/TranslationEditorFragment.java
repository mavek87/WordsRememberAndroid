package com.matteoveroni.wordsremember.dictionary.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.view.PojoManipulable;
import com.matteoveroni.wordsremember.pojos.TranslationForVocable;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class TranslationEditorFragment extends Fragment implements PojoManipulable<TranslationForVocable> {

    public static final String TAG = TagGenerator.tag(TranslationEditorFragment.class);

    private Unbinder viewInjector;
    private TranslationForVocable translationForVocable;
    private Word vocableUsedByView;

    @BindView(R.id.fragment_translation_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_translation_editor_txt_vocable_name)
    EditText txt_translationName;

    public TranslationEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation_editor, container, false);
        viewInjector = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        viewInjector.unbind();
        super.onDestroyView();
    }

    @Override
    public TranslationForVocable getPojoUsedByView() {
        // TODO how to know translation id???
        return translationForVocable;
    }

    @Override
    public void setPojoUsedInView(TranslationForVocable pojo) {
        if (pojo.getTranslation().getName().trim().isEmpty()) {
            lbl_title.setText("Create translation for " + pojo.getVocable().getName());
        } else {
            lbl_title.setText("Edit translation for " + pojo.getVocable().getName());
        }
        txt_translationName.setText(pojo.getTranslation().getName());
        translationForVocable = pojo;
    }
}
