package com.matteoveroni.wordsremember.scene_dictionary.model;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

public class DictionaryModel {

    public static final String TAG = TagGenerator.tag(DictionaryModel.class);

    @Getter
    @Setter
    private Word vocableSelected;

    @Getter
    @Setter
    private Word translationSelected;

    public void reset() {
        vocableSelected = null;
        translationSelected = null;
        Log.i(TAG, "Model resetted");
    }
}
