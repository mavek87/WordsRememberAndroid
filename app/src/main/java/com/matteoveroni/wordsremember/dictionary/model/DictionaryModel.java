package com.matteoveroni.wordsremember.dictionary.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    public static final String TAG = TagGenerator.tag(DictionaryModel.class);

    private Word lastVocableSelected;
    private Word lastTranslationSelected;

    public Word getLastVocableSelected() {
        return lastVocableSelected;
    }

    public void setLastVocableSelected(Word lastVocableSelected) {
        this.lastVocableSelected = lastVocableSelected;
    }

    public void reset() {
        lastVocableSelected = null;
        lastTranslationSelected = null;
        Log.i(TAG, "Model resetted");
    }

    public Word getLastTranslationSelected() {
        return lastTranslationSelected;
    }

    public void setLastTranslationSelected(Word lastTranslationSelected) {
        this.lastTranslationSelected = lastTranslationSelected;
    }
}
