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
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class TranslationEditorFragment extends Fragment implements PojoManipulable<VocableTranslation> {

    public static final String TAG = TagGenerator.tag(TranslationEditorFragment.class);

    private Unbinder viewInjector;
    private VocableTranslation lastValidVocableTranslationInView;

    @BindView(R.id.fragment_translation_editor_title)
    TextView lbl_title;

    @BindView(R.id.fragment_translation_editor_txt_vocable_name)
    EditText txt_translationName;

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
    public VocableTranslation getPojoUsed() {
        lastValidVocableTranslationInView.setTranslation(getTranslationInView());
        return lastValidVocableTranslationInView;
    }

    private Word getTranslationInView(){
        final long translationId = lastValidVocableTranslationInView.getTranslation().getId();
        final String translationNameFromView = txt_translationName.getText().toString();
        return new Word(translationId, translationNameFromView);
    }

    @Override
    public void setPojoUsed(VocableTranslation pojo) {
        if (pojo.getTranslation().getName().trim().isEmpty()) {
            lbl_title.setText("Create translation for " + pojo.getVocable().getName());
        } else {
            lbl_title.setText("Edit translation " + pojo.getTranslation().getName());
        }
        txt_translationName.setText(pojo.getTranslation().getName());
        this.lastValidVocableTranslationInView = pojo;
    }
}
