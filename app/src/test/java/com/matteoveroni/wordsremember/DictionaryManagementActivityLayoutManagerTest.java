package com.matteoveroni.wordsremember;

import com.matteoveroni.wordsremember.activities.dictionary_management.layout.ActivityViewLayout;
import com.matteoveroni.wordsremember.activities.dictionary_management.layout.DictionaryManagementActivityLayoutManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementActivityLayoutManagerTest {

    private DictionaryManagementActivityLayoutManager layoutManager;

    private static ActivityViewLayout VIEW_LAYOUT;
    private static ActivityViewLayout SECOND_VIEW_LAYOUT;
    private static ActivityViewLayout THIRD_VIEW_LAYOUT;

    @BeforeClass
    public static void createMockLayouts() {
        VIEW_LAYOUT = mock(ActivityViewLayout.class);
        SECOND_VIEW_LAYOUT = mock(ActivityViewLayout.class);
        THIRD_VIEW_LAYOUT = mock(ActivityViewLayout.class);
    }

    @Before
    public void init() {
        layoutManager = DictionaryManagementActivityLayoutManager.getInstance();
    }

    @Test(expected = EmptyStackException.class)
    public void readLayoutInUseIfLayoutNotSetThrowsEmptyStackException() {
        layoutManager.readLayoutInUse();
    }

    @Test
    public void readTheSavedLayoutInUseWorks() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        assertEquals(VIEW_LAYOUT, layoutManager.readLayoutInUse());
    }

    @Test(expected = EmptyStackException.class)
    public void discardCurrentLayoutAndGetPreviousOneIfAnyLayoutWasSavedThrowsEmptyStackException() {
        layoutManager.discardCurrentLayoutAndGetPreviousOne();
    }

    @Test(expected = EmptyStackException.class)
    public void discardCurrentLayoutAndGetPreviousOneIfOnlyOneLayoutWasSavedThrowsEmptyStackException() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.discardCurrentLayoutAndGetPreviousOne();
    }

    @Test
    public void discardCurrentLayoutAndGetPreviousOneIfTwoLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        ActivityViewLayout previousActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertEquals(VIEW_LAYOUT, previousActivityViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, previousActivityViewLayout);
    }

    @Test
    public void discardCurrentLayoutTwoTimesAndGetTheFirstOneIfThreeLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(THIRD_VIEW_LAYOUT);

        ActivityViewLayout previousActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, previousActivityViewLayout);
        assertEquals(SECOND_VIEW_LAYOUT, previousActivityViewLayout);
        assertNotEquals(VIEW_LAYOUT, previousActivityViewLayout);

        ActivityViewLayout firstActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, firstActivityViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, firstActivityViewLayout);
        assertEquals(VIEW_LAYOUT, firstActivityViewLayout);
    }
}
