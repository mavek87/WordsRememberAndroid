package com.matteoveroni.wordsremember.ui.layout;

import java.util.EmptyStackException;

public interface ViewLayoutManager {

    void saveLayoutInUse(ViewLayout layoutToSave);

    ViewLayout getLayout(ViewLayoutChronology layoutChronology) throws NoViewLayoutFoundException;

    enum ViewLayoutChronology {
        LAST_LAYOUT, PREVIOUS_LAYOUT;
    }

    class NoViewLayoutFoundException extends EmptyStackException {
    }
}
