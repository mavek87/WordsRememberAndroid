package com.matteoveroni.wordsremember.ui.layout;

import java.util.EmptyStackException;

public interface ViewLayoutManager {

    void saveLayoutInUse(ViewLayout layoutToSave);

    ViewLayout getViewLayout(ViewLayoutChronology layoutChronology) throws
            NullPointerException, EmptyStackException;

}
