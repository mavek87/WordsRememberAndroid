package com.matteoveroni.wordsremember.dictionary.management;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutChronology;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementViewLayoutManager implements ViewLayoutManager {

    public static final String TAG = "DMA_LAYOUT_MANAGER";

    private final Stack<ViewLayout> viewLayoutHistory = new Stack<>();

    @Override
    public void saveLayoutInUse(ViewLayout layoutToSave) {
        if (!isLayoutToSaveEqualsToCurrentLayout(layoutToSave)) {
            viewLayoutHistory.push(layoutToSave);
        }
    }

    @Override
    public ViewLayout getViewLayout(ViewLayoutChronology viewLayoutChronology) throws NullPointerException, EmptyStackException {
        ViewLayout viewLayout = null;
        switch (viewLayoutChronology) {
            case CURRENT_LAYOUT:
                viewLayout = viewLayoutHistory.peek();
                break;
            case PREVIOUS_LAYOUT:
                viewLayoutHistory.pop();
                viewLayout = viewLayoutHistory.peek();
                break;
        }
        return viewLayout;
    }

    private boolean isLayoutToSaveEqualsToCurrentLayout(ViewLayout layoutToSave) {
        if (!viewLayoutHistory.empty()) {
            ViewLayout currentDictionaryManagementActivityLayout = viewLayoutHistory.peek();
            return currentDictionaryManagementActivityLayout.equals(layoutToSave);
        }
        return false;
    }
}
