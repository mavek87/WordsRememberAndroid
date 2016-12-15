package com.matteoveroni.wordsremember.ui.layout;

import java.util.EmptyStackException;

public interface ViewLayoutManager {

    void saveLayoutInUse(ViewLayout layoutToSave);

    ViewLayout getViewLayout(ViewLayoutBackupChronology layoutChronology) throws NoViewLayoutFoundException;

    enum ViewLayoutBackupChronology {
        LAST_LAYOUT, PREVIOUS_LAYOUT;
    }

    class NoViewLayoutFoundException extends EmptyStackException {
    }
}
