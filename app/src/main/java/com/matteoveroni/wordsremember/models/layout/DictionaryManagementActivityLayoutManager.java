package com.matteoveroni.wordsremember.models.layout;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.matteoveroni.wordsremember.ui.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.ui.fragments.DictionaryManipulationFragment;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementActivityLayoutManager implements Serializable {

    public static final String TAG = "DMA_LAYOUT_MANAGER";

    private final Stack<DictionaryManagementViewLayout> layoutHistory = new Stack<>();

    private FrameLayout managementContainer;
    private FrameLayout manipulationContainer;

    public DictionaryManagementActivityLayoutManager(FrameLayout managementContainer, FrameLayout manipulationContainer) {
        this.managementContainer = managementContainer;
        this.manipulationContainer = manipulationContainer;
    }

    public void resyncWithNewViewElements(FrameLayout managementContainer, FrameLayout manipulationContainer){
        this.managementContainer = managementContainer;
        this.manipulationContainer = manipulationContainer;
    }

    /**
     * Called to avoid possible memory leaks
     */
    public void dispose() {
        managementContainer = null;
        manipulationContainer = null;
    }

    public void saveLayoutInUse(DictionaryManagementViewLayout layoutToSave) {
        if (!isLayoutToSaveEqualsToCurrentLayout(layoutToSave)) {
            layoutHistory.push(layoutToSave);
        }
    }

    public DictionaryManagementViewLayout readLayoutInUse() throws EmptyStackException {
        return layoutHistory.peek();
    }

    public DictionaryManagementViewLayout discardCurrentLayoutAndGetPreviousOne() throws EmptyStackException {
        layoutHistory.pop();
        return layoutHistory.peek();
    }

    public enum LayoutChronology {
        CURRENT, PREVIOUS;
    }

    public void restoreLayout(LayoutChronology layoutChronology) throws NullPointerException, EmptyStackException {
        DictionaryManagementViewLayout restoreLayout = null;
        switch (layoutChronology) {
            case CURRENT:
                restoreLayout = readLayoutInUse();
                break;
            case PREVIOUS:
                restoreLayout = discardCurrentLayoutAndGetPreviousOne();
                break;
        }

        switch (restoreLayout.getType()) {
            case SINGLE:
                useSingleLayoutForFragment(restoreLayout.getMainFragmentTAG());
                break;
            case TWO_COLUMNS:
                useLayoutTwoHorizontalColumns();
                break;
            case TWO_ROWS:
                useLayoutTwoVerticalRows();
                break;
        }
    }

    /**
     * Use a layout with two horizontal columns that hosts managment and manipulation fragments
     */
    public void useLayoutTwoHorizontalColumns() {
        setLayout(0, DictionaryManagementViewLayout.MATCH_PARENT, 1f, 0, DictionaryManagementViewLayout.MATCH_PARENT, 1f);
        saveLayoutInUse(new DictionaryManagementViewLayout(DictionaryManagementViewLayout.Type.TWO_COLUMNS, null));
    }

    /**
     * Use a layout with two vertical rows that hosts managment and manipulation fragments
     */
    public void useLayoutTwoVerticalRows() {
        setLayout(DictionaryManagementViewLayout.MATCH_PARENT, 0, 1f, DictionaryManagementViewLayout.MATCH_PARENT, 0, 1f);
        saveLayoutInUse(new DictionaryManagementViewLayout(DictionaryManagementViewLayout.Type.TWO_ROWS, null));
    }

    /**
     * Use a single layout with only the management fragment visible
     */
    public void useSingleLayoutForFragment(String fragmentTAG) {
        switch (fragmentTAG) {
            case DictionaryManagementFragment.TAG:
                setLayout(DictionaryManagementViewLayout.MATCH_PARENT, DictionaryManagementViewLayout.MATCH_PARENT, 0, 0);
                break;
            case DictionaryManipulationFragment.TAG:
                setLayout(0, 0, DictionaryManagementViewLayout.MATCH_PARENT, DictionaryManagementViewLayout.MATCH_PARENT);
                break;
        }
        saveLayoutInUse(new DictionaryManagementViewLayout(DictionaryManagementViewLayout.Type.SINGLE, fragmentTAG));
    }

    private void setLayout(int managementContainerWidth, int managementContainerHeight, int manipulationContainerWidth, int manipulationContainerHeight) {
        managementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight)
        );

        manipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(manipulationContainerWidth, manipulationContainerHeight)
        );
    }

    private void setLayout(
            int managementContainerWidth,
            int managementContainerHeight,
            float managementContainerWeight,
            int manipulationContainerWidth,
            int manipulationContainerHeight,
            float manipulationContainerWeight) {

        managementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight, managementContainerWeight)
        );

        manipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(manipulationContainerWidth, manipulationContainerHeight, manipulationContainerWeight)
        );
    }

    private boolean isLayoutToSaveEqualsToCurrentLayout(DictionaryManagementViewLayout layoutToSave) {
        if (!layoutHistory.empty()) {
            DictionaryManagementViewLayout currentDictionaryManagementViewLayout = layoutHistory.peek();
            return currentDictionaryManagementViewLayout.equals(layoutToSave);
        }
        return false;
    }
}
