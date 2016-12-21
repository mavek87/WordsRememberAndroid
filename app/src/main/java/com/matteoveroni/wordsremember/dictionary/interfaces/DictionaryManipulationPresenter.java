package com.matteoveroni.wordsremember.dictionary.interfaces;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManipulationPresenter extends Presenter {

    void setVocableToManipulate(Word vocableToManipulate);
    void onSaveVocableRequest();
}
