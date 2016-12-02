package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementActivityLayoutManager implements Serializable {

    public static final String TAG = "DMA_LAYOUT_MANAGER";

    private final Stack<ActivityViewLayout> layoutHistory = new Stack<>();

    private DictionaryManagementActivityLayoutManager() {
    }

    public static DictionaryManagementActivityLayoutManager getInstance() {
        return new DictionaryManagementActivityLayoutManager();
    }

    public void saveLayoutInUse(ActivityViewLayout layoutToSave) {
        if (!isLayoutToSaveEqualsToCurrentLayout(layoutToSave)) {
            layoutHistory.push(layoutToSave);
        }
    }

    public ActivityViewLayout readLayoutInUse() throws EmptyStackException {
        return layoutHistory.peek();
    }

    public ActivityViewLayout discardCurrentLayoutAndGetPreviousOne() throws EmptyStackException {
        layoutHistory.pop();
        return layoutHistory.peek();
    }

    private boolean isLayoutToSaveEqualsToCurrentLayout(ActivityViewLayout layoutToSave) {
        if (!layoutHistory.empty()) {
            ActivityViewLayout currentActivityViewLayout = layoutHistory.peek();
            return currentActivityViewLayout.equals(layoutToSave);
        }
        return false;
    }
}
