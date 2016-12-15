package com.matteoveroni.wordsremember.dictionary.management.interfaces;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManagementPresenter extends Presenter {
    void onViewRestored();

    void onViewCreatedForTheFirstTime();

    boolean onKeyBackPressedRestorePreviousState();

    void onCreateVocableRequest(Word vocable);


}
