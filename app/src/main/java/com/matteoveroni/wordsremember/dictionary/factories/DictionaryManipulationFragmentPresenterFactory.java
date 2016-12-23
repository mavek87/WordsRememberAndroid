package com.matteoveroni.wordsremember.dictionary.factories;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.DictionaryManipulationFragmentPresenter;

public class DictionaryManipulationFragmentPresenterFactory implements PresenterFactory {

    public DictionaryManipulationFragmentPresenter create() {
        return new DictionaryManipulationFragmentPresenter();
    }
}
