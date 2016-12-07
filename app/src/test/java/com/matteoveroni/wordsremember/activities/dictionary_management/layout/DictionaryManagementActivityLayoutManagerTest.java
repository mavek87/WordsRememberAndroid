package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import android.widget.FrameLayout;

import com.matteoveroni.wordsremember.models.layout.DictionaryManagementActivityLayoutManager;
import com.matteoveroni.wordsremember.models.layout.DictionaryManagementViewLayout;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementActivityLayoutManagerTest {

    private DictionaryManagementActivityLayoutManager layoutManager;

    private static DictionaryManagementViewLayout VIEW_LAYOUT;
    private static DictionaryManagementViewLayout SECOND_VIEW_LAYOUT;
    private static DictionaryManagementViewLayout THIRD_VIEW_LAYOUT;

    private static FrameLayout MANGEMENT_CONTAINER, MANIPULATION_CONTAINER;

    /**
     * Executed only the first time
     */
    @BeforeClass
    public static void createMockLayouts() {
        VIEW_LAYOUT = mock(DictionaryManagementViewLayout.class);
        SECOND_VIEW_LAYOUT = mock(DictionaryManagementViewLayout.class);
        THIRD_VIEW_LAYOUT = mock(DictionaryManagementViewLayout.class);

        MANGEMENT_CONTAINER = mock(FrameLayout.class);
        MANIPULATION_CONTAINER = mock(FrameLayout.class);
    }

    /**
     * Executed before each test
     */
    @Before
    public void init() {
        layoutManager = new DictionaryManagementActivityLayoutManager(MANGEMENT_CONTAINER, MANIPULATION_CONTAINER);
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
        final DictionaryManagementViewLayout previousDictionaryManagementViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertEquals(VIEW_LAYOUT, previousDictionaryManagementViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, previousDictionaryManagementViewLayout);
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

        final DictionaryManagementViewLayout previousViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, previousViewLayout);
        assertEquals(SECOND_VIEW_LAYOUT, previousViewLayout);
        assertNotEquals(VIEW_LAYOUT, previousViewLayout);

        final DictionaryManagementViewLayout firstViewLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
        assertNotEquals(THIRD_VIEW_LAYOUT, firstViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, firstViewLayout);
        assertEquals(VIEW_LAYOUT, firstViewLayout);
    }
}
