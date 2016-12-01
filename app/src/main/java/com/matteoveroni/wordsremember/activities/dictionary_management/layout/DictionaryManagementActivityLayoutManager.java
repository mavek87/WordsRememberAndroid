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

    public static final DictionaryManagementActivityLayoutManager getInstance() {
        return new DictionaryManagementActivityLayoutManager();
    }

    public void saveLayoutInUse(ActivityViewLayout layout) {
        layoutHistory.push(layout);
    }

    public ActivityViewLayout readLayoutInUse() {
        return layoutHistory.peek();
    }

    public ActivityViewLayout discardCurrentLayoutAndGetPreviousOne() throws EmptyStackException {
        layoutHistory.pop();
        return layoutHistory.peek();
    }
}
