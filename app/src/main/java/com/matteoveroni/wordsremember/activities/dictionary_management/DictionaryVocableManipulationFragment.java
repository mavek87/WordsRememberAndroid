package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.R;

/**
 * @author Matteo Veroni
 */

public class DictionaryVocableManipulationFragment extends Fragment {

    public static final String TAG = "F_DICTIONARY_CREATE";

    public DictionaryVocableManipulationFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment DictionaryVocableManipulationFragment.
     */
    public static DictionaryVocableManipulationFragment getInstance() {
        return new DictionaryVocableManipulationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_create_vocable, container, false);
    }


}
