package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutBackupChronology;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Matteo Veroni
 */
public class DictionaryManagementViewLayoutManagerTest {

    private DictionaryManagementViewLayoutManager layoutManager;

    private static ViewLayout VIEW_LAYOUT;
    private static ViewLayout SECOND_VIEW_LAYOUT;
    private static ViewLayout THIRD_VIEW_LAYOUT;

    /**
     * Executed only the first time
     */
    @BeforeClass
    public static void createMockLayouts() {
        VIEW_LAYOUT = mock(ViewLayout.class);
        SECOND_VIEW_LAYOUT = mock(ViewLayout.class);
        THIRD_VIEW_LAYOUT = mock(ViewLayout.class);
    }

    /**
     * Executed before each test
     */
    @Before
    public void init() {
        layoutManager = new DictionaryManagementViewLayoutManager();
    }

    @Test(expected = EmptyStackException.class)
    public void testReadLayoutInUseIfLayoutNotSetThrowsEmptyStackException() {
        layoutManager.getViewLayout(ViewLayoutBackupChronology.LAST_LAYOUT);
    }

    @Test
    public void testReadTheSavedLayoutInUseWorks() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        assertEquals(VIEW_LAYOUT, layoutManager.getViewLayout(ViewLayoutBackupChronology.LAST_LAYOUT));
    }

    @Test(expected = EmptyStackException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfAnyLayoutWasSaved() {
        layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
    }

    @Test(expected = EmptyStackException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfOnlyOneLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
    }

    @Test
    public void testDiscardCurrentLayoutAndGetPreviousOneIfTwoLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        final ViewLayout previousDictionaryManagementViewLayout = layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
        assertEquals(VIEW_LAYOUT, previousDictionaryManagementViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, previousDictionaryManagementViewLayout);
    }

    @Test(expected = EmptyStackException.class)
    public void testSameLayoutInUseCannotBeSavedMoreThanOneTimeConsequently() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
    }

    @Test
    public void testDiscardCurrentLayoutTwoTimesAndGetTheFirstOneIfThreeLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(THIRD_VIEW_LAYOUT);

        final ViewLayout previousViewLayout = layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
        assertNotEquals(THIRD_VIEW_LAYOUT, previousViewLayout);
        assertEquals(SECOND_VIEW_LAYOUT, previousViewLayout);
        assertNotEquals(VIEW_LAYOUT, previousViewLayout);

        final ViewLayout firstViewLayout = layoutManager.getViewLayout(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
        assertNotEquals(THIRD_VIEW_LAYOUT, firstViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, firstViewLayout);
        assertEquals(VIEW_LAYOUT, firstViewLayout);
    }
}
