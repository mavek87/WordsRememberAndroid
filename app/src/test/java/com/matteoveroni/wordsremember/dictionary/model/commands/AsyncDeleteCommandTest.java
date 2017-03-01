package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AsyncDeleteCommandTest {

    private ShadowApplication app;
    private ContentResolver contentResolver;

    private EventBus eventBus = EventBus.getDefault();

    private static final Uri VOCABLES_URI = VocablesContract.CONTENT_URI;

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        eventBus.removeAllStickyEvents();
    }

    @Test
    public void test_NoStickyEvent_In_EventuBus_If_AnyOperationIsComplete() {
        assertNull(
                "EventAsyncDeleteVocableCompleted should NOT be fired before onDeleteComplete",
                eventBus.getStickyEvent(EventAsyncDeleteVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncDeleteVocableCommand_Fires_EventAsyncDeleteVocableCompleted_onDeleteComplete_() {
        AsyncDeleteCommand asyncDeleteCommand = new AsyncDeleteCommand(
                contentResolver, VOCABLES_URI, "", new String[]{""}
        );

        asyncDeleteCommand.onDeleteComplete(0, null, 1);

        assertNotNull(
                "EventAsyncUpdateVocableCompleted should be fired after onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncDeleteVocableCompleted.class)
        );
    }
}
