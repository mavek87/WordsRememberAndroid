package com.matteoveroni.wordsremember.dictionary.interfaces;

import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManipulationView {
    void showMessage(String message);
    void returnToPreviousView();
    void populateViewForVocable(Word vocable);
}

