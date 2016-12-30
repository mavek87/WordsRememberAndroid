package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManagementView {

    void showMessage(String message);

    void goToManipulationView(Word vocableToManipulate);
}
