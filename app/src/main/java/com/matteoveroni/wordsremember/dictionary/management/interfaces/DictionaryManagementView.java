package com.matteoveroni.wordsremember.dictionary.management.interfaces;

import android.content.Context;

import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

public interface DictionaryManagementView {

    Context getContext();

    ViewLayout getViewLayout();

    void useSingleLayoutWithFragment(String fragmentTAG);

    void useTwoHorizontalColumnsLayout();

    void useTwoVerticalRowsLayout();

    boolean isViewLarge();

    boolean isViewLandscape();
}
