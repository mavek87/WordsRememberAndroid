package com.matteoveroni.wordsremember.dictionary.factories;

import android.support.v4.app.Fragment;

import com.matteoveroni.wordsremember.dictionary.management.fragment.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.dictionary.manipulation.DictionaryManipulationFragment;

/**
 * @author Matteo Veroni
 */
public class DictionaryFragmentFactory {

    public enum DictionaryFragmentType {
        MANAGEMENT,
        MANIPULATION
    }

    /**
     * Use this factory method to create a new instance of
     * of a DictionaryFragment.
     *
     * @return A new instance of a DictionaryFragment.
     */
    public static Fragment getInstance(DictionaryFragmentType type) {
        switch (type) {
            case MANAGEMENT:
                return new DictionaryManagementFragment();
            case MANIPULATION:
                return new DictionaryManipulationFragment();
            default:
                throw new IllegalArgumentException("Unknown DictionaryFragmentType");
        }
    }
}
