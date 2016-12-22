package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import com.matteoveroni.wordsremember.dictionary.DictionaryManagementViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.ViewLayoutChronology;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.NoViewLayoutFoundException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @Test(expected = NoViewLayoutFoundException.class)
    public void testReadLayoutInUseIfLayoutNotSetThrowsEmptyStackException() {
        layoutManager.getLayout(ViewLayoutChronology.LAST_LAYOUT);
    }

    @Test
    public void testReadTheSavedLayoutInUseWorks() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        assertEquals(VIEW_LAYOUT, layoutManager.getLayout(ViewLayoutChronology.LAST_LAYOUT));
    }

    @Test(expected = NoViewLayoutFoundException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfAnyLayoutWasSaved() {
        layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
    }

    @Test(expected = NoViewLayoutFoundException.class)
    public void testDiscardCurrentLayoutAndGetPreviousOneThrowsEmptyStackExceptionIfOnlyOneLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
    }

    @Test
    public void testDiscardCurrentLayoutAndGetPreviousOneIfTwoLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        final ViewLayout previousDictionaryManagementViewLayout = layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
        assertEquals(VIEW_LAYOUT, previousDictionaryManagementViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, previousDictionaryManagementViewLayout);
    }

    @Test(expected = NoViewLayoutFoundException.class)
    public void testSameLayoutInUseCannotBeSavedMoreThanOneTimeConsequently() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
    }

    @Test
    public void testDiscardCurrentLayoutTwoTimesAndGetTheFirstOneIfThreeLayoutWasSaved() {
        layoutManager.saveLayoutInUse(VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(SECOND_VIEW_LAYOUT);
        layoutManager.saveLayoutInUse(THIRD_VIEW_LAYOUT);

        final ViewLayout previousViewLayout = layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
        assertNotEquals(THIRD_VIEW_LAYOUT, previousViewLayout);
        assertEquals(SECOND_VIEW_LAYOUT, previousViewLayout);
        assertNotEquals(VIEW_LAYOUT, previousViewLayout);

        final ViewLayout firstViewLayout = layoutManager.getLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
        assertNotEquals(THIRD_VIEW_LAYOUT, firstViewLayout);
        assertNotEquals(SECOND_VIEW_LAYOUT, firstViewLayout);
        assertEquals(VIEW_LAYOUT, firstViewLayout);
    }
}
