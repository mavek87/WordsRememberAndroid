package com.matteoveroni.wordsremember.dictionary.management.interfaces;

import com.matteoveroni.wordsremember.Presenter;

public interface DictionaryManagementPresenter extends Presenter {

    void onViewCreatedForTheFirstTime();

    void onViewRestored();

    boolean onKeyBackPressedRestorePreviousState();
}
