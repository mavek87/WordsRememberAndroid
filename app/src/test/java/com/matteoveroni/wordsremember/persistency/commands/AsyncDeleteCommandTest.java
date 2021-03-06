package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncDeleteVocableCompleted;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AsyncDeleteCommandTest {

    private ShadowApplication app;
    private ContentResolver contentResolver;

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final Uri VOCABLES_URI = VocablesContract.CONTENT_URI;
    private static final int FAKE_NUMBER_OF_ROWS_DELETED = 1;

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        EVENT_BUS.removeAllStickyEvents();
    }

    @Test
    public void test_NoStickyEvent_In_EventBus_If_AnyOperationIsComplete() {
        assertNull(
                "EventAsyncDeleteVocableCompleted should NOT be fired before onDeleteComplete",
                EVENT_BUS.getStickyEvent(EventAsyncDeleteVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncDeleteVocableCommand_Fires_EventAsyncDeleteVocableCompleted_onDeleteComplete_() {
        AsyncDeleteCommand asyncDeleteCommand = new AsyncDeleteCommand(
                contentResolver, VOCABLES_URI, "", new String[]{""}
        );

        asyncDeleteCommand.onDeleteComplete(0, null, FAKE_NUMBER_OF_ROWS_DELETED);

        EventAsyncDeleteVocableCompleted event = EVENT_BUS.getStickyEvent(EventAsyncDeleteVocableCompleted.class);
        assertEquals(
                "EventAsyncDeleteVocableCompleted should be fired after onDeleteComplete with right number of rows deleted",
                FAKE_NUMBER_OF_ROWS_DELETED, event.getNumberOfRowsDeleted()
        );
    }
}
