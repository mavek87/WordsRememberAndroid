package com.matteoveroni.wordsremember.dictionary.interfaces;

import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

public interface DictionaryManagementView {

    ViewLayout getViewLayout();

    void useSingleLayoutWithFragment(String fragmentTAG);

    void useTwoHorizontalColumnsLayout();

    void useTwoVerticalRowsLayout();

    boolean isViewLarge();

    boolean isViewLandscape();

    void showMessage(String message);

    void goToManipulationView(Word vocableToManipulate);
}
