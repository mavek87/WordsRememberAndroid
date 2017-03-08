package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AsyncUpdateCommandTest {

    private ShadowApplication app;
    private ContentResolver contentResolver;

    private final EventBus eventBus = EventBus.getDefault();

    private static final ContentValues VALUES = new ContentValues();
    private static final Uri VOCABLES_URI = VocablesContract.CONTENT_URI;
    private static final int FAKE_NUMBER_OF_ROWS_UPDATED = 1;

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        eventBus.removeAllStickyEvents();
        VALUES.clear();
    }

    @Test
    public void test_NoStickyEvent_In_EventuBus_If_AnyOperationIsComplete() {
        assertNull(
                "EventAsyncUpdateVocableCompleted should NOT be fired before onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncUpdateVocableCommand_Fires_EventAsyncUpdateVocableCompleted_onUpdateComplete_() {
        AsyncUpdateCommand asyncUpdateCommand = new AsyncUpdateCommand(
                contentResolver, VOCABLES_URI, VALUES, "", new String[]{""}
        );

        asyncUpdateCommand.onUpdateComplete(0, null, FAKE_NUMBER_OF_ROWS_UPDATED);

        EventAsyncUpdateVocableCompleted event = eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class);
        assertEquals(
                "EventAsyncUpdateVocableCompleted should be fired onUpdateComplete with right number of rows updated",
                FAKE_NUMBER_OF_ROWS_UPDATED, event.getNumberOfVocablesUpdated()
        );
    }
}