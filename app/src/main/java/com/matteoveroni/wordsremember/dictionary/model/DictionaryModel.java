package com.matteoveroni.wordsremember.dictionary.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    public static final String TAG = TagGenerator.tag(DictionaryModel.class);

    private Word lastValidVocableSelected;
    private Word vocableInView;

    public Word getLastValidVocableSelected() {
        return lastValidVocableSelected;
    }

    public void setLastValidVocableSelected(Word lastValidVocableSelected) {
        this.lastValidVocableSelected = lastValidVocableSelected;
    }

    public Word getEditedVocableInView() {
        return vocableInView;
    }

    public void setVocableInView(Word vocableInView) {
        this.vocableInView = vocableInView;
    }

    public void reset() {
        lastValidVocableSelected = null;
        vocableInView = null;
        Log.i(TAG, "Model resetted");
    }
}
