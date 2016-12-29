package com.matteoveroni.wordsremember.dictionary;

import android.util.Log;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementViewLayoutManager implements ViewLayoutManager {

    public static final String TAG = "DictLayoutManager";

    private final Stack<ViewLayout> viewLayoutHistory = new Stack<>();

    @Override
    public void saveLayoutInUse(ViewLayout layoutToSave) {
        if (layoutToSave != null && !isLayoutToSaveEqualsToCurrentLayout(layoutToSave)) {
            viewLayoutHistory.push(layoutToSave);
            printLayoutHistoryForDebug();
        }
    }

    @Override
    public void removeLastLayoutSaved() {
        if (!viewLayoutHistory.empty()) {
            viewLayoutHistory.pop();
            printLayoutHistoryForDebug();
        }
    }

    @Override
    public ViewLayout getLayout(ViewLayoutChronology viewLayoutChronology) throws NoViewLayoutFoundException {
        ViewLayout viewLayout = null;
        try {
            switch (viewLayoutChronology) {
                case LAST_LAYOUT:
                    viewLayout = viewLayoutHistory.peek();
                    break;
                case PREVIOUS_LAYOUT:
                    viewLayoutHistory.pop();
                    viewLayout = viewLayoutHistory.peek();
                    break;
            }
        } catch (EmptyStackException ex) {
            throw new NoViewLayoutFoundException();
        }
        return viewLayout;
    }

    private boolean isLayoutToSaveEqualsToCurrentLayout(ViewLayout layoutToSave) {
        try {
            if (!viewLayoutHistory.empty()) {
                ViewLayout currentDictionaryManagementActivityLayout = viewLayoutHistory.peek();
                return currentDictionaryManagementActivityLayout.equals(layoutToSave);
            }
        } catch (EmptyStackException ex) {
        }
        return false;
    }

    private void printLayoutHistoryForDebug() {
        int i=0;
        for(ViewLayout layout : viewLayoutHistory) {
            i++;
            Log.d(TAG, i + ") layout | type: " + layout.getViewLayoutType().toString() + " - mainFragmentTAG: " + layout.getMainFragmentTAG().toString());
        }
    }
}
