package com.matteoveroni.wordsremember.scene_dictionary.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    public static final String TAG = TagGenerator.tag(DictionaryModel.class);

    private Word vocableSelected;
    private Word translationSelected;

    public Word getVocableSelected() {
        return vocableSelected;
    }

    public void setVocableSelected(Word lastVocableSelected) {
        this.vocableSelected = lastVocableSelected;
    }

    public Word getTranslationSelected() {
        return translationSelected;
    }

    public void setTranslationSelected(Word translationSelected) {
        this.translationSelected = translationSelected;
    }

    public void reset() {
        vocableSelected = null;
        translationSelected = null;
        Log.i(TAG, "Model reset");
    }
}
