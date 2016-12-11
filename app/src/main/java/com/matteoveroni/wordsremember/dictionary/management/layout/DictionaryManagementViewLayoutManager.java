package com.matteoveroni.wordsremember.dictionary.management.layout;

import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementViewLayoutManager {

    public static final String TAG = "DMA_LAYOUT_MANAGER";

    private final Stack<ViewLayout> viewLayoutHistory = new Stack<>();

    public void saveLayoutInUse(ViewLayout layoutToSave) {
        if (!isLayoutToSaveEqualsToCurrentLayout(layoutToSave)) {
            viewLayoutHistory.push(layoutToSave);
        }
    }

    public ViewLayout readLayoutInUse() throws EmptyStackException {
        return viewLayoutHistory.peek();
    }

    public ViewLayout discardCurrentLayoutAndGetPreviousOne() throws EmptyStackException {
        viewLayoutHistory.pop();
        return viewLayoutHistory.peek();
    }

    public enum LayoutChronology {
        CURRENT, PREVIOUS;
    }

    public ViewLayout getViewLayout(LayoutChronology layoutChronology) throws NullPointerException, EmptyStackException {
        ViewLayout viewLayout = null;
        switch (layoutChronology) {
            case CURRENT:
                viewLayout = readLayoutInUse();
                break;
            case PREVIOUS:
                viewLayout = discardCurrentLayoutAndGetPreviousOne();
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
