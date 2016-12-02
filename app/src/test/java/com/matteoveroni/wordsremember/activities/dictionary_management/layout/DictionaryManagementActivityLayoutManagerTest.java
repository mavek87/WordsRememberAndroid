package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import com.matteoveroni.wordsremember.activities.dictionary_management.layout.ActivityViewLayout;
import com.matteoveroni.wordsremember.activities.dictionary_management.layout.DictionaryManagementActivityLayoutManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementActivityLayoutManagerTest {

    private DictionaryManagementActivityLayoutManager layoutManager;

    private static ActivityViewLayout VIEW_LAYOUT;
    private static ActivityViewLayout SECOND_VIEW_LAYOUT;
    private static ActivityViewLayout THIRD_VIEW_LAYOUT;

    /**
     * Executed only the first time
     */
    @BeforeClass
    public static void createMockLayouts() {
        VIEW_LAYOUT = mock(ActivityViewLayout.class);
        SECOND_VIEW_LAYOUT = mock(ActivityViewLayout.class);
        THIRD_VIEW_LAYOUT = mock(ActivityViewLayout.class);
    }

    /**
     * Executed before each test
     */
    @Before
    public void init() {
        layoutManager = DictionaryManagementActivityLayoutManager.getInstance();
    }

    @Test(expected = EmptyStackException.class)
    public void testReadLayoutInUseIfLayoutNotSetThrowsEmptyStackException() {
        layoutManager.readLayoutInUse();
    }

    @Test
    public void testReadTheSavedLayoutInUseWorks() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        assertEquals(VIEW_LAYOUT, layoutManager.readLayoutInUse());
    }

    @Test(expected = EmptyStackException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfAnyLayoutWasSaved() {
        layoutManager.discardCurrentLayoutAndGetPreviousOne();
    }

    @Test(expected = EmptyStackException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfOnlyOneLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.discardCurrentLayoutAndGetPreviousOne();
    }

    @Test
    public void testDiscardCurrentLayoutAndGetPreviousOneIfTwoLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        final ActivityViewLayout previousActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertEquals(VIEW_LAYOUT, previousActivityViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, previousActivityViewLayout);
    }

    @Test(expected = EmptyStackException.class)
    public void testSameLayoutInUseCannotBeSavedMoreThanOneTimeConsequently() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.discardCurrentLayoutAndGetPreviousOne();
    }

    @Test
    public void testDiscardCurrentLayoutTwoTimesAndGetTheFirstOneIfThreeLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(THIRD_VIEW_LAYOUT);

        final ActivityViewLayout previousActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, previousActivityViewLayout);
        assertEquals(SECOND_VIEW_LAYOUT, previousActivityViewLayout);
        assertNotEquals(VIEW_LAYOUT, previousActivityViewLayout);

        final ActivityViewLayout firstActivityViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, firstActivityViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, firstActivityViewLayout);
        assertEquals(VIEW_LAYOUT, firstActivityViewLayout);
    }
}
