package com.matteoveroni.wordsremember.dictionary.interfaces;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManipulationPresenter extends Presenter {
    void onViewRestored();

    void onViewCreatedForTheFirstTime();

    void onSaveVocableRequest(Word vocableToSave);
}
