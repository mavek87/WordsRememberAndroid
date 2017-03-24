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
    private Word lastValidTranslationSelected;

    public Word getLastValidVocableSelected() {
        return lastValidVocableSelected;
    }

    public void setLastValidVocableSelected(Word lastValidVocableSelected) {
        this.lastValidVocableSelected = lastValidVocableSelected;
    }

    public void reset() {
        lastValidVocableSelected = null;
        lastValidTranslationSelected = null;
        Log.i(TAG, "Model resetted");
    }

    public Word getLastValidTranslationSelected() {
        return lastValidTranslationSelected;
    }

    public void setLastValidTranslationSelected(Word lastValidTranslationSelected) {
        this.lastValidTranslationSelected = lastValidTranslationSelected;
    }
}
